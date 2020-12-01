package com.zygne.stockanalyzer.domain.model;

import java.util.Comparator;

public class LiquiditySide {

    public static final int REJECT = 0;
    public static final int ACCEPT = 1;

    public static final int WEAK = 2;
    public static final int STRONG = 3;

    public double type;
    public double strength;
    public double start;
    public double end;
    public long volume;
    public int volumeRank;
    public double percentile;

    public long timeStamp;

    public double getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSide() {
        if (type == REJECT) {
            return "Sell";
        } else {
            return "Buy";
        }
    }

    public String getStrength() {
        if (strength == STRONG) {
            return "Strong";
        } else {
            return "Weak";
        }
    }

    public double getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public int getVolumeRank() {
        return volumeRank;
    }

    public void setVolumeRank(int volumeRank) {
        this.volumeRank = volumeRank;
    }

    public boolean inZone(double value) {

        return value <= end && value >= start;
    }

    @Override
    public String toString() {
        String value;

        if (type == REJECT) {
            value = "Reject";
        } else {
            value = "Accept";
        }

        return value + " " + start + " " + end + " " + timeStamp;
    }

    public static final class TimeComparator implements Comparator<LiquiditySide> {

        @Override
        public int compare(LiquiditySide o1, LiquiditySide o2) {
            return Long.compare(o1.timeStamp, o2.timeStamp);
        }
    }

    public static final class VolumeComparator implements Comparator<LiquiditySide> {

        @Override
        public int compare(LiquiditySide o1, LiquiditySide o2) {
            return Long.compare(o1.volume, o2.volume);
        }
    }
}
