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
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.Opcode

private const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/template/extension/kayo/KayoPatch;"

private inline fun safeHook(name: String, block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        println("Optional hook '$name' skipped: ${e.message}")
    }
}

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
        // =====================================================================
        // Mobile Specific Hooks (au.com.kayosports)
        // =====================================================================

        // 1. Capture DefaultTrackSelector instance when ProfileBasedTrackSelector (Lpx/e;) is constructed.
        safeHook("ProfileBasedTrackSelectorConstructor") {
            ProfileBasedTrackSelectorConstructorFingerprint.methodOrNull?.let { method ->
                method.addInstruction(
                    0,
                    "invoke-static {p1}, $EXTENSION_CLASS_DESCRIPTOR->onTrackSelectorInit(Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;)V"
                )
            }
        }

        // 2. Intercept ProfileBasedTrackSelector.f(Tracks) (called by onTracksChanged(Tracks))
        // to capture the available video tracks, update the in-player quality badge, and re-apply
        // any manual resolution lock.
        safeHook("ProfileBasedTrackSelectorUpdateTracks") {
            ProfileBasedTrackSelectorUpdateTracksFingerprint.methodOrNull?.let { method ->
                val fp = ProfileBasedTrackSelectorUpdateTracksFingerprint
                val selectorFieldIndex = fp.instructionMatches[1].index
                val selectorFieldRef = method.getInstruction<ReferenceInstruction>(selectorFieldIndex).reference as FieldReference

                method.addInstructions(
                    0,
                    """
                        iget-object v0, p0, $selectorFieldRef
                        invoke-static {v0, p1}, $EXTENSION_CLASS_DESCRIPTOR->onTracksChanged(Lcom/google/android/exoplayer2/trackselection/DefaultTrackSelector;Lcom/google/android/exoplayer2/Tracks;)Z
                    """
                )
            }
        }

        // 3. Prevent Kayo's automatic TrackBitrateLimiter / ProfileBitrate from overriding
        // a manually selected video resolution in b(int maxVideoBitrate) and c(int maxVideoHeight).
        safeHook("ProfileBasedTrackSelectorSetMaxBitrate") {
            ProfileBasedTrackSelectorSetMaxBitrateFingerprint.methodOrNull?.let { method ->
                method.addInstructionsWithLabels(
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
            }
        }

        // Neutralize Kayo's client-side maxVideoHeight cap so ExoPlayer is never constrained to 720p
        safeHook("ProfileBasedTrackSelectorSetMaxSize") {
            ProfileBasedTrackSelectorSetMaxSizeFingerprint.methodOrNull?.let { method ->
                method.addInstruction(
                    0,
                    "return-void"
                )
            }
        }

        // 4. Force the top-bar track selector button (toggle_track_selector) to always be visible
        // on live streams (Kayo normally hides it on live streams when there is only 1 audio track).
        safeHook("AbstractDaznPlayerControlsTrackSelectorVisibility") {
            AbstractDaznPlayerControlsTrackSelectorVisibilityFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        invoke-static {p1}, $EXTENSION_CLASS_DESCRIPTOR->overrideTrackSelectorVisibility(Z)Z
                        move-result p1
                    """
                )
            }
        }

        // 5. Inject our live resolution pill badge ("AUTO (1080p)" / "1080p50" / "4K") and attach the
        // Video Quality selector menu click listener after AbstractDaznPlayerControls sets up
        // getTrackSelectorButton().setOnClickListener(this).
        safeHook("AbstractDaznPlayerControlsSetup") {
            AbstractDaznPlayerControlsSetupFingerprint.methodOrNull?.let { method ->
                val fp = AbstractDaznPlayerControlsSetupFingerprint
                val moveResultIndex = fp.instructionMatches[3].index
                val setOnClickIndex = fp.instructionMatches[4].index
                val buttonRegister = method.getInstruction<OneRegisterInstruction>(moveResultIndex).registerA

                method.addInstruction(
                    setOnClickIndex + 1,
                    "invoke-static {v$buttonRegister}, $EXTENSION_CLASS_DESCRIPTOR->onControlsInitialized(Landroid/view/View;)V"
                )
            }
        }

        // 6. Also hook DefaultPlayerControls (v2 controls) if present in the APK.
        safeHook("DefaultPlayerControlsConstructor") {
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
        }

        // 7. Force full playback capabilities ("mta,50fps,lowlatency,hevc,hdr") in the stream request
        safeHook("PlaybackServiceDelegateCapabilities") {
            PlaybackServiceDelegateCapabilitiesFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        const-string v0, "mta,50fps,lowlatency,hevc,hdr"
                        return-object v0
                    """
                )

                // 8. Prevent sending "low" DRM tier in the playback request body (which restricts CDN to 720p)
                safeHook("PlaybackServiceDelegateDrmTier") {
                    PlaybackServiceDelegateDrmTierFingerprint.methodOrNull?.let { drmTierMethod ->
                        drmTierMethod.addInstructions(
                            0,
                            """
                                const/4 v0, 0x0
                                return-object v0
                            """
                        )
                    }
                }
            }
        }

        // 9. Force DrmTierService.b() to return false (not low tier)
        safeHook("DrmTierServiceIsLowTier") {
            DrmTierServiceIsLowTierFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        const/4 v0, 0x0
                        return v0
                    """
                )
            }
        }

        // 10. Force HEVC decoder support check to return true
        safeHook("PlaybackCodecsServiceIsHevcSupported") {
            PlaybackCodecsServiceIsHevcSupportedFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        const/4 v0, 0x1
                        return v0
                    """
                )
            }
        }

        // 11. Capture active ExoPlayer instance when attached in PlayerInterfaceDazn.R1(ExoPlayer)
        safeHook("PlayerInterfaceAttachExoPlayer") {
            PlayerInterfaceAttachExoPlayerFingerprint.methodOrNull?.let { method ->
                method.addInstruction(
                    0,
                    "invoke-static {p1}, $EXTENSION_CLASS_DESCRIPTOR->onExoPlayerAttached(Lcom/google/android/exoplayer2/ExoPlayer;)V"
                )
            }
        }

        // =====================================================================
        // Android TV Specific Hooks (au.com.kayosports.tv)
        // =====================================================================

        // 12. Hook VideoCapabilitiesPlugin.execute to spoof 4K, 60fps, Widevine L1, HDCP 2.2, HEVC, and HDR
        safeHook("TvVideoCapabilitiesPluginExecute") {
            TvVideoCapabilitiesPluginExecuteFingerprint.methodOrNull?.let { method ->
                method.addInstructionsWithLabels(
                    0,
                    """
                        invoke-static {p1, p2, p3}, $EXTENSION_CLASS_DESCRIPTOR->onVideoCapabilitiesExecute(Ljava/lang/String;Lorg/json/JSONArray;Ljava/lang/Object;)Z
                        move-result v0
                        if-eqz v0, :cond_native_exec
                        const/4 v0, 0x1
                        return v0
                        :cond_native_exec
                        nop
                    """
                )
            }
        }

        // 13. Hook DrmPlayer.setMaxVideoBitrate to override/uncap max bitrate on Android TV,
        // capture DrmPlayer and bandwidth meter, and configure builder for 4K UHD video parameters
        safeHook("TvDrmPlayerSetMaxBitrate") {
            TvDrmPlayerSetMaxBitrateFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        invoke-static {p0}, $EXTENSION_CLASS_DESCRIPTOR->onDrmPlayerAttached(Ljava/lang/Object;)V
                        invoke-static {p1}, $EXTENSION_CLASS_DESCRIPTOR->overrideTvMaxBitrate(I)I
                        move-result p1
                    """
                )

                val fp = TvDrmPlayerSetMaxBitrateFingerprint
                if (fp.instructionMatches.size >= 2) {
                    val iputIndex = fp.instructionMatches[1].index
                    val builderRegister = method.getInstruction<TwoRegisterInstruction>(iputIndex).registerB
                    method.addInstruction(
                        iputIndex + 1,
                        "invoke-static {v$builderRegister}, $EXTENSION_CLASS_DESCRIPTOR->configureTvTrackParametersBuilder(Ljava/lang/Object;)V"
                    )
                }
            }
        }

        // 14. Hook PlayerView.setPlayer to capture active Media3 ExoPlayer instance and attach TV Quality Button
        safeHook("TvPlayerViewSetPlayer") {
            TvPlayerViewSetPlayerFingerprint.methodOrNull?.let { method ->
                method.addInstruction(
                    0,
                    "invoke-static {p0, p1}, $EXTENSION_CLASS_DESCRIPTOR->onTvPlayerViewSetPlayer(Landroid/view/View;Ljava/lang/Object;)V"
                )
            }
        }

        // 14b. Hook ExoPlayerEventsListener.onTracksChanged to capture active Tracks and Player on Android TV
        safeHook("TvExoPlayerEventsListenerOnTracksChanged") {
            TvExoPlayerEventsListenerOnTracksChangedFingerprint.methodOrNull?.let { method ->
                method.addInstruction(
                    0,
                    "invoke-static {p1, p0}, $EXTENSION_CLASS_DESCRIPTOR->onTvTracksChanged(Ljava/lang/Object;Ljava/lang/Object;)V"
                )
            }
        }

        // 15. Hook MainActivity.dispatchKeyEvent to handle Android TV remote keys (Info, Menu, Color keys)
        // for toggling Live Stream Diagnostics HUD and Quality Menu.
        safeHook("TvMainActivityDispatchKeyEvent") {
            TvMainActivityDispatchKeyEventFingerprint.methodOrNull?.let { method ->
                method.addInstructionsWithLabels(
                    0,
                    """
                        invoke-static {p0, p1}, $EXTENSION_CLASS_DESCRIPTOR->onTvKeyEvent(Landroid/app/Activity;Landroid/view/KeyEvent;)Z
                        move-result v0
                        if-eqz v0, :cond_native_key
                        const/4 v0, 0x1
                        return v0
                        :cond_native_key
                        nop
                    """
                )
            }
        }

        // 16. Hook VideoPlugin.getPhysicalDisplaySize to return 3840x2160 (4K UHD)
        safeHook("TvVideoPluginGetPhysicalDisplaySize") {
            TvVideoPluginGetPhysicalDisplaySizeFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->getPhysicalDisplaySizeSpoof()Lorg/json/JSONObject;
                        move-result-object v0
                        return-object v0
                    """
                )
            }
        }

        // 17. Hook DaznDrmSessionManager constructor to force useWidevineL3 = false (guaranteeing Widevine L1)
        safeHook("TvDaznDrmSessionManagerConstructor") {
            TvDaznDrmSessionManagerConstructorFingerprint.methodOrNull?.let { method ->
                method.addInstruction(
                    0,
                    "const/4 p3, 0x0"
                )
            }
        }

        // 18. Hook Util.getCurrentDisplayModeSize to report 3840x2160 (4K UHD) display mode size
        safeHook("TvMedia3UtilGetCurrentDisplayModeSize") {
            TvMedia3UtilGetCurrentDisplayModeSizeFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        new-instance v0, Landroid/graphics/Point;
                        const/16 v1, 0x0f00
                        const/16 v2, 0x0870
                        invoke-direct {v0, v1, v2}, Landroid/graphics/Point;-><init>(II)V
                        return-object v0
                    """
                )
            }
        }

        // 19. Hook DefaultBandwidthMeter.getInitialBitrateEstimateForNetworkType to return 100 Mbps
        safeHook("TvDefaultBandwidthMeterGetInitialEstimate") {
            TvDefaultBandwidthMeterGetInitialEstimateFingerprint.methodOrNull?.let { method ->
                method.addInstructions(
                    0,
                    """
                        const-wide/32 v0, 0x05f5e100
                        return-wide v0
                    """
                )
            }
        }

        // 20. Hook ResultKt.parse(JSONObject)LoadArguments to sanitize useWidevineL3 and maxVideoBitrate
        safeHook("TvLoadArgumentsParser") {
            TvLoadArgumentsParserFingerprint.methodOrNull?.let { method ->
                method.addInstruction(
                    0,
                    "invoke-static {p0}, $EXTENSION_CLASS_DESCRIPTOR->sanitizeLoadArgumentsJson(Lorg/json/JSONObject;)V"
                )
            }
        }

        // 21. Hook DefaultTrackSelector.selectTracksForType to enforce single-track selection when quality is locked
        safeHook("TvDefaultTrackSelectorSelectTracksForType") {
            TvDefaultTrackSelectorSelectTracksForTypeFingerprint.methodOrNull?.let { method ->
                val pairCreateIdx = method.implementation?.instructions?.indexOfFirst { insn ->
                    insn.opcode == Opcode.INVOKE_STATIC && insn.toString().contains("Pair;->create")
                } ?: -1
                if (pairCreateIdx >= 0) {
                    method.addInstructions(
                        pairCreateIdx + 2,
                        """
                            invoke-static {v0, p0}, $EXTENSION_CLASS_DESCRIPTOR->overrideSelectTracksResult(Landroid/util/Pair;I)Landroid/util/Pair;
                            move-result-object v0
                        """
                    )
                }
            }
        }

    }
}

