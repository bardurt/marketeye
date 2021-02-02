package com.zygne.stockanalyzer.domain.model;

import java.util.List;

public class VolumeBarDetails {

    private double high;
    private double low;
    private long volume;
    private int rank;
    private long timeStamp;
    private List<LiquidityLevel> supply;

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<LiquidityLevel> getSupply() {
        return supply;
    }

    public void setSupply(List<LiquidityLevel> supply) {
        this.supply = supply;
    }
}
