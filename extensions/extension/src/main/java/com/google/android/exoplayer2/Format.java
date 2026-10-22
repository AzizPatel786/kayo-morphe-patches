package com.google.android.exoplayer2;

/**
 * Compile-time stub for ExoPlayer Format.
 */
public final class Format {
    public static final int NO_VALUE = -1;

    public final String id;
    public final String label;
    public final String language;
    public final int averageBitrate;
    public final int peakBitrate;
    public final int bitrate;
    public final String codecs;
    public final String sampleMimeType;
    public final int width;
    public final int height;
    public final float frameRate;

    private Format() {
        this.id = null;
        this.label = null;
        this.language = null;
        this.averageBitrate = NO_VALUE;
        this.peakBitrate = NO_VALUE;
        this.bitrate = NO_VALUE;
        this.codecs = null;
        this.sampleMimeType = null;
        this.width = NO_VALUE;
        this.height = NO_VALUE;
        this.frameRate = NO_VALUE;
    }
}
