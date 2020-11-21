package com.zygne.stockalyze.domain.model.enums;

public enum TimeFrame {
    One_Minute("1 Minute"),
    Three_Minutes("3 Minutes"),
    Five_Minutes("5 Minutes"),
    Fifteen_Minutes("15 Minutes"),
    Thirty_Minutes("30 Minutes"),
    Hour("60 Minutes"),
    Day("Daily");

    public final String label;

    TimeFrame(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
