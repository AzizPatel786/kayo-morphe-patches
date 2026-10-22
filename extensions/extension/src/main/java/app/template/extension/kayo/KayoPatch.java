package app.template.extension.kayo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionOverride;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Extension helper class for Kayo Sports In-Player Quality Menu patch.
 *
 * Hooks into Kayo's ProfileBasedTrackSelector (Lpx/e;) and player controls
 * (AbstractDaznPlayerControls / DefaultPlayerControls) to provide an interactive
 * resolution/bitrate selector and prevent Kayo's TrackBitrateLimiter from
 * overriding a manually selected resolution.
 */
@SuppressWarnings("unused")
public final class KayoPatch {

    private static final String PREFS_NAME = "morphe_kayo_quality_prefs";
    private static final String KEY_REMEMBER_QUALITY = "remember_quality";
    private static final String KEY_SELECTED_HEIGHT = "selected_height";
    private static final String KEY_SELECTED_BITRATE = "selected_bitrate";

    private static final int VIDEO_TRACK_TYPE = 2; // C.TRACK_TYPE_VIDEO
    private static final int QUALITY_AUTO = -1;
    private static final int QUALITY_BUTTON_VIEW_ID = 0x7f0b9999;

    private static volatile DefaultTrackSelector currentTrackSelector;
    private static volatile Tracks currentTracks;

    // -1 means Auto (Adaptive), > 0 means target video height (e.g. 1080, 720, 540)
    private static volatile int selectedHeight = QUALITY_AUTO;
    // Specific bitrate to lock to (or -1 if any bitrate at that height)
    private static volatile int selectedBitrate = QUALITY_AUTO;
    private static volatile boolean prefsLoaded = false;

    private static final List<WeakReference<TextView>> qualityBadgeViews = new ArrayList<>();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    private KayoPatch() {
    }

    /**
     * Represents a single selectable video track option from ExoPlayer's Tracks.
     */
    private static final class VideoTrackOption {
        final TrackGroup trackGroup;
        final int trackIndex;
        final Format format;
        final int width;
        final int height;
        final int bitrate;
        final float frameRate;
        final boolean isCurrentlyPlaying;
        final boolean isSupported;

        VideoTrackOption(
                TrackGroup trackGroup,
                int trackIndex,
                Format format,
                boolean isCurrentlyPlaying,
                boolean isSupported
        ) {
            this.trackGroup = trackGroup;
            this.trackIndex = trackIndex;
            this.format = format;
            this.width = format.width > 0 ? format.width : 0;
            this.height = format.height > 0 ? format.height : 0;
            this.bitrate = format.bitrate > 0 ? format.bitrate
                    : (format.peakBitrate > 0 ? format.peakBitrate : format.averageBitrate);
            this.frameRate = format.frameRate > 0 ? format.frameRate : 0f;
            this.isCurrentlyPlaying = isCurrentlyPlaying;
            this.isSupported = isSupported;
        }

        String getDisplayLabel() {
            StringBuilder sb = new StringBuilder();
            if (height >= 2160) {
                sb.append(height).append("p (4K UHD)");
            } else if (height > 0) {
                sb.append(height).append("p");
            } else if (width > 0) {
                sb.append(width).append("w");
            } else {
                sb.append("Track ").append(trackIndex + 1);
            }

            if (frameRate > 0f) {
                int fps = Math.round(frameRate);
                if (fps >= 48) {
                    sb.append(fps); // e.g. 2160p50, 1080p50, 720p50
                } else {
                    sb.append(" ").append(fps).append("fps");
                }
            }

            if (bitrate > 0) {
                double mbps = bitrate / 1_000_000.0;
                sb.append(String.format(Locale.US, "  •  %.2f Mbps", mbps));
            }

            if (format.codecs != null) {
                if (format.codecs.contains("hev") || format.codecs.contains("hvc")) {
                    sb.append(" (HEVC)");
                }
                if (format.codecs.contains("hdr") || format.codecs.contains("dovi") || format.codecs.contains("dvhe")) {
                    sb.append(" [HDR]");
                }
            }

            if (isCurrentlyPlaying) {
                sb.append("  [Playing]");
            }

            return sb.toString();
        }

        String getShortBadgeLabel() {
            if (height >= 2160) {
                int fps = Math.round(frameRate);
                if (fps >= 48) {
                    return "4K" + fps;
                }
                return "4K";
            }
            if (height > 0) {
                int fps = Math.round(frameRate);
                if (fps >= 48) {
                    return height + "p" + fps;
                }
                return height + "p";
            }
            if (bitrate > 0) {
                return String.format(Locale.US, "%.1fM", bitrate / 1_000_000.0);
            }
            return "MAN";
        }
    }

    // =========================================================================
    // Bytecode Hook Entrypoints for ProfileBasedTrackSelector (Lpx/e;)
    // =========================================================================

    /**
     * Called at the end of {@code Lpx/e;-><init>(DefaultTrackSelector, Lpx/g, Lpx/i)}.
     */
    public static void onTrackSelectorInit(DefaultTrackSelector selector) {
        if (selector != null) {
            currentTrackSelector = selector;
            try {
                DefaultTrackSelector.Parameters.Builder builder = selector.buildUponParameters();
                builder.clearViewportSizeConstraints();
                builder.clearVideoSizeConstraints();
                builder.setExceedVideoConstraintsIfNecessary(true);
                builder.setExceedRendererCapabilitiesIfNecessary(true);
                selector.setParameters(builder.build());
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Ensures all high-definition and 4K capabilities are reported in the playback request body.
     */
    public static String spoofPlaybackCapabilities(String original) {
        if (original == null || original.isEmpty()) {
            return "mta,50fps,lowlatency,hevc,hdr";
        }
        StringBuilder sb = new StringBuilder(original);
        if (!original.contains(",50fps")) {
            sb.append(",50fps");
        }
        if (!original.contains(",hevc")) {
            sb.append(",hevc");
        }
        if (!original.contains(",hdr")) {
            sb.append(",hdr");
        }
        return sb.toString();
    }

    /**
     * Called at the start of {@code Lpx/e;->onTracksChanged(Tracks)}.
     *
     * @return {@code true} if manual quality is locked (so the caller skips Kayo's
     *         automatic profile bitrate cap), {@code false} if Auto is active.
     */
    public static boolean onTracksChanged(DefaultTrackSelector selector, Tracks tracks) {
        if (selector != null) {
            currentTrackSelector = selector;
        }
        if (tracks != null) {
            currentTracks = tracks;
        }

        updateAllQualityBadges();

        if (selectedHeight != QUALITY_AUTO && currentTrackSelector != null && currentTracks != null) {
            applySelectedQuality(currentTrackSelector, currentTracks);
            return true;
        }
        return false;
    }

    /**
     * Called at the start of {@code Lpx/e;->b(int maxVideoBitrate)} and {@code Lpx/e;->c(int maxVideoHeight)}.
     *
     * @return {@code true} if manual quality is locked (blocking Kayo's bitrate/size limiter
     *         from overwriting our selection), {@code false} to allow normal adaptive behavior.
     */
    public static boolean shouldIgnoreBitrateLimit() {
        if (selectedHeight != QUALITY_AUTO && currentTrackSelector != null && currentTracks != null) {
            applySelectedQuality(currentTrackSelector, currentTracks);
            return true;
        }
        return false;
    }

    // =========================================================================
    // Bytecode Hook Entrypoints for Player Controls UI
    // =========================================================================

    /**
     * Called from {@code AbstractDaznPlayerControls.j4(...)} and {@code DefaultPlayerControls.<init>(...)}
     * when the player controls view is initialized.
     *
     * 1. Loads persisted quality preference if enabled.
     * 2. Injects a dedicated "HD / Quality" pill button right next to the track selector button
     *    inside the top-right controls bar.
     * 3. Also attaches a long-click listener on the existing track selector button as a fallback.
     */
    public static void onControlsInitialized(final View trackSelectorButton) {
        if (trackSelectorButton == null) {
            return;
        }

        final Context context = trackSelectorButton.getContext();
        loadPrefsIfNeeded(context);

        // Always make the track selector button visible so the user can access it on live streams
        trackSelectorButton.setVisibility(View.VISIBLE);

        // Find the enclosing player controls view (which implements View.OnClickListener in AbstractDaznPlayerControls)
        final View.OnClickListener originalClickListener = findParentOnClickListener(trackSelectorButton);

        // Clicking the track selector button opens the Video Quality menu (with an "Audio / CC" button if original listener exists)
        trackSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Runnable openOriginal = null;
                if (originalClickListener != null) {
                    openOriginal = new Runnable() {
                        @Override
                        public void run() {
                            originalClickListener.onClick(v);
                        }
                    };
                }
                showQualityDialog(v.getContext(), openOriginal);
            }
        });

        // Long-pressing the audio/track selector button also opens the Quality dialog directly
        trackSelectorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showQualityDialog(v.getContext(), null);
                return true;
            }
        });

        // Inject a dedicated Quality badge button into the track selector button
        injectQualityButton(trackSelectorButton);
    }

    private static View.OnClickListener findParentOnClickListener(View view) {
        android.view.ViewParent parent = view.getParent();
        while (parent instanceof View) {
            if (parent instanceof View.OnClickListener) {
                return (View.OnClickListener) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * Called when {@code AbstractDaznPlayerControls.o2(boolean visible)} is invoked.
     * Forces the track selector button to remain visible on all streams (live & VOD).
     */
    public static boolean overrideTrackSelectorVisibility(boolean originalVisible) {
        return true;
    }

    /**
     * Called when the user clicks the track selector button in {@code AbstractDaznPlayerControls.onClick(View)}
     * or {@code DefaultPlayerControls}.
     *
     * Shows our Video Quality selector dialog with an option to also open the original Audio & Subtitles menu.
     *
     * @param view The clicked view (or player controls view).
     * @return {@code true} to consume the click and show the Quality menu.
     */
    public static boolean onTrackSelectorClicked(final View view) {
        if (view == null) {
            return false;
        }
        showQualityDialog(view.getContext(), null);
        return true;
    }

    // =========================================================================
    // Quality Selection & Track Override Logic
    // =========================================================================

    private static List<VideoTrackOption> extractVideoTracks(Tracks tracks) {
        List<VideoTrackOption> options = new ArrayList<>();
        if (tracks == null) {
            return options;
        }

        try {
            List<Tracks.Group> groups = tracks.getGroups();
            if (groups == null) {
                return options;
            }

            for (int g = 0; g < groups.size(); g++) {
                Tracks.Group group = groups.get(g);
                if (group == null || group.getType() != VIDEO_TRACK_TYPE) {
                    continue;
                }

                TrackGroup mediaTrackGroup = group.getMediaTrackGroup();
                int length = group.length;
                for (int i = 0; i < length; i++) {
                    Format format = group.getTrackFormat(i);
                    if (format == null) {
                        continue;
                    }
                    boolean isSelected = group.isTrackSelected(i);
                    boolean isSupported = group.isTrackSupported(i);
                    options.add(new VideoTrackOption(mediaTrackGroup, i, format, isSelected, isSupported));
                }
            }
        } catch (Throwable ignored) {
        }

        // Sort highest resolution first, then highest frameRate, then highest bitrate
        Collections.sort(options, new Comparator<VideoTrackOption>() {
            @Override
            public int compare(VideoTrackOption a, VideoTrackOption b) {
                if (b.height != a.height) {
                    return Integer.compare(b.height, a.height);
                }
                if (Float.compare(b.frameRate, a.frameRate) != 0) {
                    return Float.compare(b.frameRate, a.frameRate);
                }
                return Integer.compare(b.bitrate, a.bitrate);
            }
        });

        return options;
    }

    private static void applySelectedQuality(DefaultTrackSelector selector, Tracks tracks) {
        if (selector == null) {
            return;
        }

        try {
            DefaultTrackSelector.Parameters.Builder builder = selector.buildUponParameters();

            if (selectedHeight == QUALITY_AUTO) {
                // Restore adaptive streaming across all video tracks
                builder.clearOverridesOfType(VIDEO_TRACK_TYPE)
                        .clearVideoSizeConstraints()
                        .clearViewportSizeConstraints()
                        .setMinVideoBitrate(0)
                        .setMaxVideoBitrate(Integer.MAX_VALUE)
                        .setExceedVideoConstraintsIfNecessary(true)
                        .setExceedRendererCapabilitiesIfNecessary(true)
                        .setForceHighestSupportedBitrate(false);
                selector.setParameters(builder.build());
                updateAllQualityBadges();
                return;
            }

            List<VideoTrackOption> videoOptions = extractVideoTracks(tracks);
            VideoTrackOption bestMatch = null;

            // 1. Try exact height + bitrate match
            for (VideoTrackOption opt : videoOptions) {
                if (opt.height == selectedHeight && (selectedBitrate <= 0 || opt.bitrate == selectedBitrate)) {
                    bestMatch = opt;
                    break;
                }
            }

            // 2. Fallback: same height with highest bitrate
            if (bestMatch == null) {
                for (VideoTrackOption opt : videoOptions) {
                    if (opt.height == selectedHeight) {
                        bestMatch = opt;
                        break;
                    }
                }
            }

            // 3. Fallback: closest available resolution if stream doesn't have exact height
            if (bestMatch == null && !videoOptions.isEmpty()) {
                int minDiff = Integer.MAX_VALUE;
                for (VideoTrackOption opt : videoOptions) {
                    int diff = Math.abs(opt.height - selectedHeight);
                    if (diff < minDiff) {
                        minDiff = diff;
                        bestMatch = opt;
                    }
                }
            }

            builder.clearOverridesOfType(VIDEO_TRACK_TYPE)
                    .clearViewportSizeConstraints()
                    .setExceedVideoConstraintsIfNecessary(true)
                    .setExceedRendererCapabilitiesIfNecessary(true);

            if (bestMatch != null && bestMatch.trackGroup != null) {
                TrackSelectionOverride override = new TrackSelectionOverride(
                        bestMatch.trackGroup,
                        bestMatch.trackIndex
                );
                builder.setOverrideForType(override);

                int targetBitrate = bestMatch.bitrate > 0 ? bestMatch.bitrate : Integer.MAX_VALUE;
                int targetWidth = bestMatch.width > 0 ? bestMatch.width : Integer.MAX_VALUE;
                int targetHeight = bestMatch.height > 0 ? bestMatch.height : selectedHeight;

                builder.setMinVideoSize(bestMatch.width > 0 ? bestMatch.width : 0, targetHeight)
                        .setMaxVideoSize(targetWidth, targetHeight)
                        .setMinVideoBitrate(bestMatch.bitrate > 0 ? bestMatch.bitrate : 0)
                        .setMaxVideoBitrate(targetBitrate);
            } else {
                // Tracks not populated yet; constrain by height
                builder.setMinVideoSize(0, selectedHeight)
                        .setMaxVideoSize(Integer.MAX_VALUE, selectedHeight)
                        .setMinVideoBitrate(selectedBitrate > 0 ? selectedBitrate : 0)
                        .setMaxVideoBitrate(selectedBitrate > 0 ? selectedBitrate : Integer.MAX_VALUE);
            }

            selector.setParameters(builder.build());
            updateAllQualityBadges();
        } catch (Throwable ignored) {
        }
    }

    // =========================================================================
    // UI Injection & Quality Dialog
    // =========================================================================

    private static void injectQualityButton(final View trackSelectorButton) {
        try {
            ViewGroup parent = (ViewGroup) trackSelectorButton.getParent();
            if (parent == null) {
                return;
            }

            // Check if already injected in this view hierarchy
            View existing = parent.findViewById(QUALITY_BUTTON_VIEW_ID);
            if (existing != null) {
                return;
            }

            Context context = trackSelectorButton.getContext();

            // Create a sleek pill badge inside the trackSelectorButton LinearLayout (or alongside it)
            // Notice in view_dazn_player_controls.xml, toggle_track_selector is a LinearLayout
            // containing an AppCompatImageView! Adding a TextView label inside toggle_track_selector
            // (or in menu_group) avoids any ConstraintLayout positioning issues!
            if (trackSelectorButton instanceof LinearLayout) {
                LinearLayout buttonLayout = (LinearLayout) trackSelectorButton;
                buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                buttonLayout.setGravity(Gravity.CENTER);

                ViewGroup.LayoutParams lp = buttonLayout.getLayoutParams();
                if (lp != null) {
                    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    buttonLayout.setLayoutParams(lp);
                }
                int padH = dpToPx(context, 8);
                buttonLayout.setPadding(padH, buttonLayout.getPaddingTop(), padH, buttonLayout.getPaddingBottom());

                TextView badge = createQualityBadgeTextView(context);
                badge.setId(QUALITY_BUTTON_VIEW_ID);
                buttonLayout.addView(badge);

                synchronized (qualityBadgeViews) {
                    qualityBadgeViews.add(new WeakReference<>(badge));
                }
                updateQualityBadgeText(badge);
            }
        } catch (Throwable ignored) {
        }
    }

    private static TextView createQualityBadgeTextView(Context context) {
        TextView tv = new TextView(context);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);

        int padH = dpToPx(context, 6);
        int padV = dpToPx(context, 2);
        tv.setPadding(padH, padV, padH, padV);

        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(dpToPx(context, 4));
        bg.setColor(Color.parseColor("#CC05D669")); // Kayo green accent with slight transparency
        tv.setBackground(bg);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = dpToPx(context, 4);
        tv.setLayoutParams(params);

        return tv;
    }

    private static void updateAllQualityBadges() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (qualityBadgeViews) {
                    for (int i = qualityBadgeViews.size() - 1; i >= 0; i--) {
                        TextView tv = qualityBadgeViews.get(i).get();
                        if (tv == null) {
                            qualityBadgeViews.remove(i);
                        } else {
                            updateQualityBadgeText(tv);
                        }
                    }
                }
            }
        });
    }

    private static void updateQualityBadgeText(TextView badge) {
        if (badge == null) {
            return;
        }
        if (selectedHeight == QUALITY_AUTO) {
            // Show AUTO or currently active adaptive resolution
            VideoTrackOption playing = findCurrentlyPlayingTrack( extractVideoTracks(currentTracks) );
            if (playing != null && playing.height > 0) {
                badge.setText("AUTO (" + playing.height + "p)");
            } else {
                badge.setText("AUTO");
            }
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(dpToPx(badge.getContext(), 4));
            bg.setColor(Color.parseColor("#99333333"));
            badge.setBackground(bg);
        } else {
            badge.setText(selectedHeight + "p");
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(dpToPx(badge.getContext(), 4));
            bg.setColor(Color.parseColor("#DD05D669"));
            badge.setBackground(bg);
        }
    }

    private static VideoTrackOption findCurrentlyPlayingTrack(List<VideoTrackOption> options) {
        for (VideoTrackOption opt : options) {
            if (opt.isCurrentlyPlaying) {
                return opt;
            }
        }
        return null;
    }

    /**
     * Displays the interactive Video Quality selector dialog.
     */
    public static void showQualityDialog(final Context rawContext, final Runnable onOpenOriginalTrackSelector) {
        final Activity activity = findActivity(rawContext);
        final Context context = activity != null ? activity : rawContext;
        if (context == null) {
            return;
        }

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    buildAndShowDialog(context, onOpenOriginalTrackSelector);
                } catch (Throwable t) {
                    Toast.makeText(context, "Unable to open quality menu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void buildAndShowDialog(final Context context, final Runnable onOpenOriginalTrackSelector) {
        loadPrefsIfNeeded(context);

        final List<VideoTrackOption> videoOptions = extractVideoTracks(currentTracks);

        // If the stream hasn't reported Tracks yet or is still loading, provide standard Kayo fallback presets
        final boolean hasLiveTracks = !videoOptions.isEmpty();

        final List<String> labels = new ArrayList<>();
        final List<Integer> heights = new ArrayList<>();
        final List<Integer> bitrates = new ArrayList<>();

        VideoTrackOption activeTrack = findCurrentlyPlayingTrack(videoOptions);
        String autoLabel = "Auto (Adaptive)";
        if (activeTrack != null && activeTrack.height > 0) {
            double mbps = activeTrack.bitrate > 0 ? activeTrack.bitrate / 1_000_000.0 : 0;
            if (mbps > 0) {
                autoLabel = String.format(Locale.US, "Auto (Adaptive  •  Currently %dp @ %.2f Mbps)", activeTrack.height, mbps);
            } else {
                autoLabel = "Auto (Adaptive  •  Currently " + activeTrack.height + "p)";
            }
        }

        labels.add(autoLabel);
        heights.add(QUALITY_AUTO);
        bitrates.add(QUALITY_AUTO);

        int checkedItem = 0;

        if (hasLiveTracks) {
            for (int i = 0; i < videoOptions.size(); i++) {
                VideoTrackOption opt = videoOptions.get(i);
                labels.add(opt.getDisplayLabel());
                heights.add(opt.height);
                bitrates.add(opt.bitrate);

                if (selectedHeight != QUALITY_AUTO) {
                    if (opt.height == selectedHeight && (selectedBitrate <= 0 || opt.bitrate == selectedBitrate)) {
                        checkedItem = i + 1;
                    } else if (checkedItem == 0 && opt.height == selectedHeight) {
                        checkedItem = i + 1;
                    }
                }
            }
        } else {
            // Fallback presets if opened before tracks are ready
            int[] presetHeights = {2160, 1080, 720, 540, 360, 288};
            String[] presetNames = {
                    "2160p (4K UHD)",
                    "1080p50 (Full HD)",
                    "720p50 (HD)",
                    "540p (SD)",
                    "360p (Low)",
                    "288p (Data Saver)"
            };
            for (int i = 0; i < presetHeights.length; i++) {
                labels.add(presetNames[i]);
                heights.add(presetHeights[i]);
                bitrates.add(QUALITY_AUTO);
                if (selectedHeight == presetHeights[i]) {
                    checkedItem = i + 1;
                }
            }
        }

        CharSequence[] items = labels.toArray(new CharSequence[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("Stream Video Quality");

        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int chosenHeight = heights.get(which);
                int chosenBitrate = bitrates.get(which);

                selectedHeight = chosenHeight;
                selectedBitrate = chosenBitrate;
                saveSelectedQuality(context, chosenHeight, chosenBitrate);

                if (currentTrackSelector != null) {
                    applySelectedQuality(currentTrackSelector, currentTracks);
                } else {
                    updateAllQualityBadges();
                }

                String toastMsg = (chosenHeight == QUALITY_AUTO)
                        ? "Video quality set to Auto (Adaptive)"
                        : "Locked video quality to " + labels.get(which).replace("  [Playing]", "");
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        final boolean remember = isRememberQualityEnabled(context);
        builder.setNeutralButton(
                remember ? "Remember: ON" : "Remember: OFF",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean newState = !remember;
                        setRememberQualityEnabled(context, newState);
                        if (newState) {
                            saveSelectedQuality(context, selectedHeight, selectedBitrate);
                        }
                        Toast.makeText(
                                context,
                                "Remember quality across streams: " + (newState ? "ON" : "OFF"),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        if (onOpenOriginalTrackSelector != null) {
            builder.setPositiveButton("Audio / CC", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onOpenOriginalTrackSelector.run();
                }
            });
        }

        builder.setNegativeButton("Close", null);
        builder.show();
    }

    // =========================================================================
    // Persistence & Utilities
    // =========================================================================

    private static void loadPrefsIfNeeded(Context context) {
        if (prefsLoaded || context == null) {
            return;
        }
        prefsLoaded = true;
        try {
            SharedPreferences prefs = context.getApplicationContext()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean remember = prefs.getBoolean(KEY_REMEMBER_QUALITY, true);
            if (remember) {
                selectedHeight = prefs.getInt(KEY_SELECTED_HEIGHT, QUALITY_AUTO);
                selectedBitrate = prefs.getInt(KEY_SELECTED_BITRATE, QUALITY_AUTO);
            }
        } catch (Throwable ignored) {
        }
    }

    private static boolean isRememberQualityEnabled(Context context) {
        try {
            SharedPreferences prefs = context.getApplicationContext()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return prefs.getBoolean(KEY_REMEMBER_QUALITY, true);
        } catch (Throwable ignored) {
            return true;
        }
    }

    private static void setRememberQualityEnabled(Context context, boolean enabled) {
        try {
            SharedPreferences prefs = context.getApplicationContext()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putBoolean(KEY_REMEMBER_QUALITY, enabled).apply();
        } catch (Throwable ignored) {
        }
    }

    private static void saveSelectedQuality(Context context, int height, int bitrate) {
        try {
            SharedPreferences prefs = context.getApplicationContext()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (prefs.getBoolean(KEY_REMEMBER_QUALITY, true)) {
                prefs.edit()
                        .putInt(KEY_SELECTED_HEIGHT, height)
                        .putInt(KEY_SELECTED_BITRATE, bitrate)
                        .apply();
            }
        } catch (Throwable ignored) {
        }
    }

    private static Activity findActivity(Context context) {
        Context current = context;
        while (current instanceof ContextWrapper) {
            if (current instanceof Activity) {
                return (Activity) current;
            }
            current = ((ContextWrapper) current).getBaseContext();
        }
        return null;
    }

    private static int dpToPx(Context context, int dp) {
        return Math.round(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        context.getResources().getDisplayMetrics()
                )
        );
    }
}
