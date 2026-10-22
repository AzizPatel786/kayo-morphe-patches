package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.TrackGroup;
import com.google.common.collect.ImmutableList;

/**
 * Compile-time stub for ExoPlayer Tracks and Tracks.Group.
 */
public final class Tracks {

    public ImmutableList<Group> getGroups() {
        throw new UnsupportedOperationException("Stub");
    }

    public static final class Group {
        public final int length;

        public Group() {
            this.length = 0;
        }

        public TrackGroup getMediaTrackGroup() {
            throw new UnsupportedOperationException("Stub");
        }

        public Format getTrackFormat(int trackIndex) {
            throw new UnsupportedOperationException("Stub");
        }

        public int getTrackSupport(int trackIndex) {
            throw new UnsupportedOperationException("Stub");
        }

        public int getType() {
            throw new UnsupportedOperationException("Stub");
        }

        public boolean isSelected() {
            throw new UnsupportedOperationException("Stub");
        }

        public boolean isTrackSelected(int trackIndex) {
            throw new UnsupportedOperationException("Stub");
        }

        public boolean isTrackSupported(int trackIndex) {
            throw new UnsupportedOperationException("Stub");
        }
    }
}
