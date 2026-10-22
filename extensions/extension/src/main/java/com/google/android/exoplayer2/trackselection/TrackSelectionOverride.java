package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.TrackGroup;
import java.util.List;

/**
 * Compile-time stub for ExoPlayer TrackSelectionOverride.
 */
public final class TrackSelectionOverride {
    public final TrackGroup mediaTrackGroup;

    public TrackSelectionOverride(TrackGroup mediaTrackGroup, int trackIndex) {
        this.mediaTrackGroup = mediaTrackGroup;
    }

    public TrackSelectionOverride(TrackGroup mediaTrackGroup, List<Integer> trackIndices) {
        this.mediaTrackGroup = mediaTrackGroup;
    }

    public int getType() {
        throw new UnsupportedOperationException("Stub");
    }
}
