package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;

/**
 * Compile-time stub for ExoPlayer TrackGroup.
 */
public final class TrackGroup {
    public final int length;
    public final String id;
    public final int type;

    public TrackGroup(Format... formats) {
        this.length = formats.length;
        this.id = "";
        this.type = 2;
    }

    public Format getFormat(int index) {
        throw new UnsupportedOperationException("Stub");
    }
}
