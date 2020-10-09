package com.zygne.stockalyze.ui.uimodel;

public enum News {
    None(0),
    Positive(1),
    Negative(-1);

    private final int value;

    private News(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
