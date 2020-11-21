package com.zygne.stockalyze.domain.model;

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
}
