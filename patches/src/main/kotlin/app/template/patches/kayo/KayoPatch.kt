package app.template.patches.kayo

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_KAYO_SPORTS
import app.template.patches.shared.Constants.COMPATIBILITY_KAYO_SPORTS_TV
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/template/extension/kayo/KayoPatch;"

@Suppress("unused")
val inPlayerQualityMenuPatch = bytecodePatch(
    name = "In-player quality menu",
    description = "Adds a menu that lists the available video qualities so the user can pick a specific resolution when watching a live stream on Kayo Sports instead of adaptive streaming.",
    default = true
) {
    compatibleWith(
        COMPATIBILITY_KAYO_SPORTS,
        COMPATIBILITY_KAYO_SPORTS_TV
    )

    extendWith("extensions/extension.mpe")

    execute {
        // 1. Capture DefaultTrackSelector instance when ProfileBasedTrackSelector (Lpx/e;) is constructed.
        ProfileBasedTrackSelectorConstructorFingerprint.method.addInstruction(
            0,
            "invoke-static {p1}, $EXTENSION_CLASS_DESCRIPTOR->onTrackSelectorInit(Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;)V"
        )

        // 2. Intercept ProfileBasedTrackSelector.f(Tracks) (called by onTracksChanged(Tracks))
        // to capture the available video tracks, update the in-player quality badge, and re-apply
        // any manual resolution lock.
        ProfileBasedTrackSelectorUpdateTracksFingerprint.let { fp ->
            val selectorFieldIndex = fp.instructionMatches[1].index
            val selectorFieldRef = fp.method.getInstruction<ReferenceInstruction>(selectorFieldIndex).reference as FieldReference

            fp.method.addInstructions(
                0,
                """
                    iget-object v0, p0, $selectorFieldRef
                    invoke-static {v0, p1}, $EXTENSION_CLASS_DESCRIPTOR->onTracksChanged(Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;Lcom/google/android/exoplayer2/Tracks;)Z
                """
            )
        }

        // 3. Prevent Kayo's automatic TrackBitrateLimiter / ProfileBitrate from overriding
        // a manually selected video resolution in b(int maxVideoBitrate) and c(int maxVideoHeight).
        ProfileBasedTrackSelectorSetMaxBitrateFingerprint.method.addInstructionsWithLabels(
            0,
            """
                invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->shouldIgnoreBitrateLimit()Z
                move-result v0
                if-eqz v0, :allow_adaptive_bitrate
                return-void
                :allow_adaptive_bitrate
                nop
            """
        )

        // Neutralize Kayo's client-side maxVideoHeight cap so ExoPlayer is never constrained to 720p
        ProfileBasedTrackSelectorSetMaxSizeFingerprint.method.addInstruction(
            0,
            "return-void"
        )

        // 4. Force the top-bar track selector button (toggle_track_selector) to always be visible
        // on live streams (Kayo normally hides it on live streams when there is only 1 audio track).
        AbstractDaznPlayerControlsTrackSelectorVisibilityFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p1}, $EXTENSION_CLASS_DESCRIPTOR->overrideTrackSelectorVisibility(Z)Z
                move-result p1
            """
        )

        // 5. Inject our live resolution pill badge ("AUTO (1080p)" / "1080p50" / "4K") and attach the
        // Video Quality selector menu click listener after AbstractDaznPlayerControls sets up
        // getTrackSelectorButton().setOnClickListener(this).
        AbstractDaznPlayerControlsSetupFingerprint.let { fp ->
            val moveResultIndex = fp.instructionMatches[3].index
            val setOnClickIndex = fp.instructionMatches[4].index
            val buttonRegister = fp.method.getInstruction<OneRegisterInstruction>(moveResultIndex).registerA

            fp.method.addInstruction(
                setOnClickIndex + 1,
                "invoke-static {v$buttonRegister}, $EXTENSION_CLASS_DESCRIPTOR->onControlsInitialized(Landroid/view/View;)V"
            )
        }

        // 6. Also hook DefaultPlayerControls (v2 controls) if present in the APK.
        DefaultPlayerControlsConstructorFingerprint.methodOrNull?.let { method ->
            val fp = DefaultPlayerControlsConstructorFingerprint
            val moveResultIndex = fp.instructionMatches[2].index
            val setOnClickIndex = fp.instructionMatches[3].index
            val buttonRegister = method.getInstruction<OneRegisterInstruction>(moveResultIndex).registerA

            method.addInstruction(
                setOnClickIndex + 1,
                "invoke-static {v$buttonRegister}, $EXTENSION_CLASS_DESCRIPTOR->onControlsInitialized(Landroid/view/View;)V"
            )
        }

        // 7. Force full playback capabilities ("mta,50fps,lowlatency,hevc,hdr") in the stream request
        PlaybackServiceDelegateCapabilitiesFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "mta,50fps,lowlatency,hevc,hdr"
                return-object v0
            """
        )

        // 8. Prevent sending "low" DRM tier in the playback request body (which restricts CDN to 720p)
        PlaybackServiceDelegateDrmTierFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return-object v0
            """
        )

        // 9. Force DrmTierService.b() to return false (not low tier)
        DrmTierServiceIsLowTierFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """
        )

        // 10. Force HEVC decoder support check to return true
        PlaybackCodecsServiceIsHevcSupportedFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """
        )
    }
}
