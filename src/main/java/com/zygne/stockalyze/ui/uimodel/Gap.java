package com.zygne.stockalyze.ui.uimodel;

public enum Gap {
    None(0),
    Up(1),
    Down(-1);

    private final int value;

    private Gap(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
