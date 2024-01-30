package com.zygne.data.domain.model;

public class Asset {

    private String displayName;
    private String brokerName;
    private Classification assetClassification;

    public Asset(String displayName, String brokerName, Classification assetClassification) {
        this.displayName = displayName;
        this.brokerName = brokerName;
        this.assetClassification = assetClassification;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Classification getAssetClass() {
        return assetClassification;
    }

    public void setAssetClass(Classification assetClassification) {
        this.assetClassification = assetClassification;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public enum Classification {
        STOCK,
        FUTURE
    }
}
