package com.zygne.stockanalyzer.domain.model.enums;

public enum DataProvider {
    ALPHA_VANTAGE("Alpha Vantage"),
    INTERACTIVE_BROKERS("Interactive Brokers");

    private final String label;

    DataProvider(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
