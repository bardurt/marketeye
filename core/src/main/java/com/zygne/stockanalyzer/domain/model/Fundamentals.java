package com.zygne.stockanalyzer.domain.model;

import com.google.gson.annotations.SerializedName;

public class Fundamentals {

    @SerializedName("SharesOutstanding")
    private int sharesOutstanding;

    @SerializedName("SharesFloat")
    private int sharesFloat;

    @SerializedName("52WeekLow")
    private double low;

    @SerializedName("52WeekHigh")
    private double high;

    @SerializedName("Name")
    private String companyName;

    @SerializedName("Exchange")
    private String exchange;

    @SerializedName("SharesShort")
    private int sharesShort;

    private double shortPercentage;

    private long avgVol;

    public int getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(int sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public int getSharesFloat() {
        return sharesFloat;
    }

    public void setSharesFloat(int sharesFloat) {
        this.sharesFloat = sharesFloat;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getSharesShort() {
        return sharesShort;
    }

    public void setSharesShort(int sharesShort) {
        this.sharesShort = sharesShort;
    }

    public long getAvgVol() {
        return avgVol;
    }

    public void setAvgVol(long avgVol) {
        this.avgVol = avgVol;
    }

    public double getShortPercentage() {
        return shortPercentage;
    }

    public void setShortPercentage(double shortPercentage) {
        this.shortPercentage = shortPercentage;
    }

    public void calculate() {
        shortPercentage = (sharesShort / (double) sharesFloat) * 100;
    }
}
