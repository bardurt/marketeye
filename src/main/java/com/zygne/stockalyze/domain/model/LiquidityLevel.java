package com.zygne.stockalyze.domain.model;

import java.util.Comparator;

public class LiquidityLevel {

    public final double price;
    private long volume;
    private double relativeVolume;
    private double volumePercentage;
    private double powerRatio;
    private boolean origin = false;
    private int rank = 0;
    private double percentile = 0.0d;
    private double breakPoint;

    public final String note = "";

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

    public boolean isOrigin() {
        return origin;
    }

    public void setOrigin(boolean origin) {
        this.origin = origin;
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

    public String getNote() {
        return note;
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

    public static final class RankComparator implements Comparator<LiquidityLevel> {

        @Override
        public int compare(LiquidityLevel o1, LiquidityLevel o2) {
            return Integer.compare(o1.rank, o2.rank);
        }
    }
}
