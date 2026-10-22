package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.TrackGroup;

/**
 * Compile-time stub for ExoPlayer DefaultTrackSelector.
 */
public class DefaultTrackSelector {

    public Parameters getParameters() {
        throw new UnsupportedOperationException("Stub");
    }

    public void setParameters(TrackSelectionParameters parameters) {
        throw new UnsupportedOperationException("Stub");
    }

    public void setParameters(Parameters.Builder parametersBuilder) {
        throw new UnsupportedOperationException("Stub");
    }

    public Parameters.Builder buildUponParameters() {
        throw new UnsupportedOperationException("Stub");
    }

    public static final class Parameters extends TrackSelectionParameters {
        public Builder buildUpon() {
            throw new UnsupportedOperationException("Stub");
        }

        public static final class Builder extends TrackSelectionParameters.Builder {
            @Override
            public Builder setMaxVideoSize(int maxVideoWidth, int maxVideoHeight) {
                return this;
            }

            @Override
            public Builder setMinVideoSize(int minVideoWidth, int minVideoHeight) {
                return this;
            }

            @Override
            public Builder setMaxVideoBitrate(int maxVideoBitrate) {
                return this;
            }

            @Override
            public Builder setMinVideoBitrate(int minVideoBitrate) {
                return this;
            }

            @Override
            public Builder clearVideoSizeConstraints() {
                return this;
            }

            @Override
            public Builder clearViewportSizeConstraints() {
                return this;
            }

            public Builder setForceHighestSupportedBitrate(boolean forceHighestSupportedBitrate) {
                return this;
            }

            public Builder setExceedVideoConstraintsIfNecessary(boolean exceedVideoConstraintsIfNecessary) {
                return this;
            }

            public Builder setExceedRendererCapabilitiesIfNecessary(boolean exceedRendererCapabilitiesIfNecessary) {
                return this;
            }

            @Override
            public Builder addOverride(TrackSelectionOverride override) {
                return this;
            }

            @Override
            public Builder setOverrideForType(TrackSelectionOverride override) {
                return this;
            }

            @Override
            public Builder clearOverride(TrackGroup mediaTrackGroup) {
                return this;
            }

            @Override
            public Builder clearOverridesOfType(int trackType) {
                return this;
            }

            @Override
            public Builder clearOverrides() {
                return this;
            }

            @Override
            public Parameters build() {
                throw new UnsupportedOperationException("Stub");
            }
        }
    }
}
