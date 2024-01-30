package com.zygne.data.domain.model.enums;

public enum TimeInterval {
    Day("Daily"),
    Week("Weekly"),
    Month("Monthly");

    private final String label;

    TimeInterval(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
