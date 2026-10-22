package app.template.patches.kayo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

/**
 * Matches `ProfileBasedTrackSelector.<init>(DefaultTrackSelector, ProfileBitrate, TrackBitrateLimiter)`
 * (`Lpx/e;-><init>` in Kayo v6.1.3).
 */
object ProfileBasedTrackSelectorConstructorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf(
        "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;",
        "L",
        "L"
    ),
    filters = listOf(
        string("defaultSelector"),
        string("profileBitrate"),
        string("trackBitrateLimiter")
    )
)

/**
 * Matches `ProfileBasedTrackSelector.onTracksChanged(Tracks)`
 * (`Lpx/e;->onTracksChanged(Lcom/google/android/exoplayer2/Tracks;)V` in Kayo v6.1.3).
 */
object ProfileBasedTrackSelectorOnTracksChangedFingerprint : Fingerprint(
    name = "onTracksChanged",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("Lcom/google/android/exoplayer2/Tracks;"),
    filters = listOf(
        string("tracks"),
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            definingClass = "this",
            type = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;"
        )
    )
)

/**
 * Wait: in `Lpx/e;`, `onTracksChanged(Tracks)` calls `f(Tracks)` directly, whereas `a:DefaultTrackSelector`
 * is accessed in `b(I)`, `c(I)`, `f(Tracks)`, `getTrackSelector()`.
 * Let's match `onTracksChanged(Lcom/google/android/exoplayer2/Tracks;)V` on the class matched by
 * [ProfileBasedTrackSelectorConstructorFingerprint], or match `f(Lcom/google/android/exoplayer2/Tracks;)V`!
 */
object ProfileBasedTrackSelectorUpdateTracksFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Lcom/google/android/exoplayer2/Tracks;"),
    filters = listOf(
        methodCall(
            definingClass = "Lcom/google/android/exoplayer2/Tracks;",
            name = "getGroups"
        ),
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            definingClass = "this",
            type = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;"
        ),
        fieldAccess(
            opcode = Opcode.IGET,
            definingClass = "Lcom/google/android/exoplayer2/trackselection/TrackSelectionParameters;",
            name = "maxVideoBitrate"
        )
    )
)

/**
 * Matches `ProfileBasedTrackSelector.b(int maxVideoBitrate)` (`Lpx/e;->b(I)V` in Kayo v6.1.3),
 * which sets `maxVideoBitrate` on `DefaultTrackSelector`.
 */
object ProfileBasedTrackSelectorSetMaxBitrateFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("I"),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            definingClass = "this",
            type = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;"
        ),
        methodCall(
            definingClass = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;",
            name = "buildUponParameters"
        ),
        methodCall(
            definingClass = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector\$Parameters\$Builder;",
            name = "setMaxVideoBitrate",
            parameters = listOf("I")
        ),
        methodCall(
            definingClass = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;",
            name = "setParameters"
        )
    )
)

/**
 * Matches `ProfileBasedTrackSelector.c(int maxVideoHeight)` (`Lpx/e;->c(I)V` in Kayo v6.1.3),
 * which sets `maxVideoSize` on `DefaultTrackSelector`.
 */
object ProfileBasedTrackSelectorSetMaxSizeFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("I"),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            definingClass = "this",
            type = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;"
        ),
        methodCall(
            definingClass = "Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector\$Parameters\$Builder;",
            name = "setMaxVideoSize",
            parameters = listOf("I", "I")
        )
    )
)

/**
 * Matches `AbstractDaznPlayerControls.j4(...)` where player control button click listeners
 * (including `getTrackSelectorButton()`) are initialized.
 */
object AbstractDaznPlayerControlsSetupFingerprint : Fingerprint(
    definingClass = "Lcom/dazn/player/controls/AbstractDaznPlayerControls;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    filters = listOf(
        string("keyMomentsPresenter"),
        string("markersController"),
        methodCall(
            definingClass = "Lcom/dazn/player/controls/AbstractDaznPlayerControls;",
            name = "getTrackSelectorButton",
            returnType = "Landroid/view/View;"
        ),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately()),
        methodCall(
            definingClass = "Landroid/view/View;",
            name = "setOnClickListener",
            location = MatchAfterImmediately()
        )
    )
)

/**
 * Matches `AbstractDaznPlayerControls.o2(boolean visible)` which toggles visibility of
 * `getTrackSelectorButton()`.
 */
object AbstractDaznPlayerControlsTrackSelectorVisibilityFingerprint : Fingerprint(
    definingClass = "Lcom/dazn/player/controls/AbstractDaznPlayerControls;",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("Z"),
    filters = listOf(
        methodCall(
            definingClass = "Lcom/dazn/player/controls/AbstractDaznPlayerControls;",
            name = "getTrackSelectorButton",
            returnType = "Landroid/view/View;"
        ),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately()),
        opcode(Opcode.INVOKE_STATIC, MatchAfterImmediately()),
        opcode(Opcode.RETURN_VOID, MatchAfterImmediately())
    )
)

/**
 * Matches `AbstractDaznPlayerControls.onClick(View)` where `getTrackSelectorButton()` click
 * dispatches `d$G`.
 */
object AbstractDaznPlayerControlsOnClickFingerprint : Fingerprint(
    definingClass = "Lcom/dazn/player/controls/AbstractDaznPlayerControls;",
    name = "onClick",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("Landroid/view/View;"),
    filters = listOf(
        methodCall(
            definingClass = "Lcom/dazn/player/controls/AbstractDaznPlayerControls;",
            name = "getTrackSelectorButton",
            returnType = "Landroid/view/View;"
        ),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately()),
        methodCall(
            definingClass = "Landroid/view/View;",
            name = "getId",
            returnType = "I",
            location = MatchAfterImmediately()
        )
    )
)

/**
 * Matches `DefaultPlayerControls.<init>(Context, AttributeSet)` (v2 player controls)
 * where `getTrackSelectorButton()` is initialized.
 */
object DefaultPlayerControlsConstructorFingerprint : Fingerprint(
    definingClass = "Lcom/dazn/player/v2/controls/DefaultPlayerControls;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf("Landroid/content/Context;", "Landroid/util/AttributeSet;"),
    filters = listOf(
        string("exoTrackSelector"),
        methodCall(
            definingClass = "Lcom/dazn/player/v2/controls/DefaultPlayerControls;",
            name = "getTrackSelectorButton"
        ),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately()),
        methodCall(
            definingClass = "Landroid/view/View;",
            name = "setOnClickListener"
        )
    )
)

/**
 * Matches `PlaybackServiceDelegate.t()` (`Lgv/x;->t()Ljava/lang/String;`),
 * which computes the stream capabilities string ("mta,50fps,lowlatency,hevc,hdr") sent in the playback request body.
 */
object PlaybackServiceDelegateCapabilitiesFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("mta"),
        string(",50fps"),
        string(",lowlatency")
    )
)

/**
 * Matches `PlaybackServiceDelegate.u()` (`Lgv/x;->u()Ljava/lang/String;`),
 * which sends "low" tier when DrmTierService reports low tier.
 */
object PlaybackServiceDelegateDrmTierFingerprint : Fingerprint(
    classFingerprint = PlaybackServiceDelegateCapabilitiesFingerprint,
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("low")
    )
)

/**
 * Matches `DrmTierService.b()` (`Lge/i;->b()Z`),
 * which determines whether the device is considered low DRM tier (e.g. failing external HDCP display check).
 */
object DrmTierServiceIsLowTierFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        string("Error determining DRM tier, defaulting to Low: "),
        string("DrmTier")
    )
)

/**
 * Matches `PlaybackCodecsService.a()` (`LXu/b;->a()Z`),
 * which checks if HEVC decoder is supported.
 */
object PlaybackCodecsServiceIsHevcSupportedFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        string("video/hevc"),
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            type = "Ls6/p;"
        )
    )
)

/**
 * Matches the class implementing `LHv/G;` (PlayerInterfaceDazn) that creates and holds the ExoPlayer instance.
 */
object PlayerInterfaceClassFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    filters = listOf(
        string("playbackAnalyticsSender"),
        string("exoplayerFactoryProvider"),
        string("trackSelector")
    )
)

/**
 * Matches `LHv/P;->R1(Lcom/google/android/exoplayer2/ExoPlayer;)V` which attaches the active ExoPlayer.
 */
object PlayerInterfaceAttachExoPlayerFingerprint : Fingerprint(
    classFingerprint = PlayerInterfaceClassFingerprint,
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("Lcom/google/android/exoplayer2/ExoPlayer;"),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IPUT_OBJECT,
            definingClass = "this",
            type = "Lcom/google/android/exoplayer2/ExoPlayer;"
        )
    )
)

