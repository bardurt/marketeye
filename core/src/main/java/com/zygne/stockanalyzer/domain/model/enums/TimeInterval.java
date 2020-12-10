package com.zygne.stockanalyzer.domain.model.enums;

public enum TimeInterval {
    One_Minute("1 Minute"),
    Three_Minutes("3 Minutes"),
    Five_Minutes("5 Minutes"),
    Fifteen_Minutes("15 Minutes"),
    Thirty_Minutes("30 Minutes"),
    Hour("60 Minutes"),
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
