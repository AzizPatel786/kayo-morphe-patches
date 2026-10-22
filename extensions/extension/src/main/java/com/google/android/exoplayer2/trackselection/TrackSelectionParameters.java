package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.TrackGroup;

/**
 * Compile-time stub for ExoPlayer TrackSelectionParameters.
 */
public class TrackSelectionParameters {
    public final int maxVideoWidth;
    public final int maxVideoHeight;
    public final int maxVideoFrameRate;
    public final int maxVideoBitrate;
    public final int minVideoWidth;
    public final int minVideoHeight;
    public final int minVideoFrameRate;
    public final int minVideoBitrate;

    protected TrackSelectionParameters() {
        this.maxVideoWidth = Integer.MAX_VALUE;
        this.maxVideoHeight = Integer.MAX_VALUE;
        this.maxVideoFrameRate = Integer.MAX_VALUE;
        this.maxVideoBitrate = Integer.MAX_VALUE;
        this.minVideoWidth = 0;
        this.minVideoHeight = 0;
        this.minVideoFrameRate = 0;
        this.minVideoBitrate = 0;
    }

    public static class Builder {
        public Builder setMaxVideoSize(int maxVideoWidth, int maxVideoHeight) {
            return this;
        }

        public Builder setMinVideoSize(int minVideoWidth, int minVideoHeight) {
            return this;
        }

        public Builder setMaxVideoBitrate(int maxVideoBitrate) {
            return this;
        }

        public Builder setMinVideoBitrate(int minVideoBitrate) {
            return this;
        }

        public Builder clearVideoSizeConstraints() {
            return this;
        }

        public Builder addOverride(TrackSelectionOverride override) {
            return this;
        }

        public Builder setOverrideForType(TrackSelectionOverride override) {
            return this;
        }

        public Builder clearOverride(TrackGroup mediaTrackGroup) {
            return this;
        }

        public Builder clearOverridesOfType(int trackType) {
            return this;
        }

        public Builder clearOverrides() {
            return this;
        }

        public TrackSelectionParameters build() {
            throw new UnsupportedOperationException("Stub");
        }
    }
}
