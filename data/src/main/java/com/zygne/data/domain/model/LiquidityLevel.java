package com.zygne.data.domain.model;

import java.util.Comparator;

public class LiquidityLevel {

    public final double price;
    private long volume;
    private double relativeVolume;
    private double volumePercentage;
    private double powerRatio;
    private int rank = 0;
    private double percentile = 0.0d;
    private double breakPoint;
    private int hits = 0;

    public double getPrice() {
        return price;
    }

    public long getVolume() {
        return volume;
    }

    public double getRelativeVolume() {
        return relativeVolume;
    }

    public void setRelativeVolume(double relativeVolume) {
        this.relativeVolume = relativeVolume;
    }

    public double getVolumePercentage() {
        return volumePercentage;
    }

    public void setVolumePercentage(double volumePercentage) {
        this.volumePercentage = volumePercentage;
    }

    public double getPowerRatio() {
        return powerRatio;
    }

    public void setPowerRatio(double powerRatio) {
        this.powerRatio = powerRatio;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getBreakPoint() {
        return breakPoint;
    }

    public void setBreakPoint(double breakPoint) {
        this.breakPoint = breakPoint;
    }

    public LiquidityLevel(double price, long volume) {
        this.price = price;
        this.volume = volume;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public static final class VolumeComparator implements Comparator<LiquidityLevel> {

        @Override
        public int compare(LiquidityLevel o1, LiquidityLevel o2) {
            return Long.compare(o1.volume, o2.volume);
        }
    }

    public static final class PriceComparator implements Comparator<LiquidityLevel> {

        @Override
        public int compare(LiquidityLevel o1, LiquidityLevel o2) {
            return Double.compare(o1.price, o2.price);
        }
    }
}
