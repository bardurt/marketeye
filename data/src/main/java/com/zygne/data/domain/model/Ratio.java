package com.zygne.data.domain.model;

public class Ratio {

    private final int closeAbove;
    private final int closeBelow;
    private final int bullish;
    private final int bearish;
    private final float closeRatio;
    private final float bullishRatio;

    public Ratio(int closeAbove, int closeBelow, int bullish, int bearish) {
        this.closeAbove = closeAbove;
        this.closeBelow = closeBelow;
        this.bullish = bullish;
        this.bearish = bearish;

        this.closeRatio = closeAbove / (float)(closeAbove + closeBelow);
        this.bullishRatio = bullish / (float)(bullish + bearish);
    }

    public int getCloseAbove() {
        return closeAbove;
    }

    public int getCloseBelow() {
        return closeBelow;
    }

    public int getBullish() {
        return bullish;
    }

    public int getBearish() {
        return bearish;
    }

    public float getCloseRatio() {
        return closeRatio;
    }

    public float getBullishRatio() {
        return bullishRatio;
    }
}
