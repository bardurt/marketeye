package com.zygne.data.domain.model.enums;

public enum DataProvider {
    YAHOO_FINANCE("Yahoo Finance");

    private final String label;

    DataProvider(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
