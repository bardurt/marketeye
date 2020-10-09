package com.zygne.stockalyze.ui.uimodel;

public enum Trend {
    Consolidating(0),
    Bullish(1),
    Bearish(-1);

    private final int value;

    private Trend(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
