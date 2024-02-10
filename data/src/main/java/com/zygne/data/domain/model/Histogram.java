package com.zygne.data.domain.model;

import com.zygne.data.domain.utils.NumberHelper;

import java.util.Calendar;
import java.util.Comparator;

public class Histogram {
    public String dateTime;
    public long timeStamp;
    public double open;
    public double high;
    public double low;
    public double close;
    public long volume;
    public long volumeSma;
    public double volumeSmaPercentile;

    public Direction getDirection() {
        if (open < close) {
            return Direction.Up;
        } else {
            return Direction.Down;
        }
    }

    public double getTotalRange() {
        return ((high - low) / low) * 100;
    }

    public double getBodyRange() {
        return ((close - open) / open) * 100;
    }

    public double getOpenHighRange() {
        return ((high - open) / open) * 100;
    }

    public enum Direction {Up, Down}

    public boolean intersects(double value) {
        if (value < high) {
            return value > low;
        }

        return false;
    }

    public boolean contains(double value) {
        if (value <= high) {
            return value >= low;
        }

        return false;
    }

    public boolean inBody(double value) {
        if (getDirection() == Direction.Up) {
            if (value <= close) {
                return value >= open;
            }
        } else {
            if (value >= close) {
                return value <= open;
            }
        }

        return false;
    }

    public double getOpenCloseChange() {
        return NumberHelper.getPercentChange(open, close);
    }

    public double getOpenHighChange() {
        return NumberHelper.getPercentChange(low, high);
    }

    @Override
    public boolean equals(Object obj) {
        long timeStamp1 = ((Histogram) obj).timeStamp;
        return timeStamp == timeStamp1;
    }

    public static final class TimeComparator implements Comparator<Histogram> {

        @Override
        public int compare(Histogram o1, Histogram o2) {
            return Long.compare(o1.timeStamp, o2.timeStamp);
        }
    }

    public static final class VolumeComparator implements Comparator<Histogram> {

        @Override
        public int compare(Histogram o1, Histogram o2) {
            return Long.compare(o1.volume, o2.volume);
        }
    }

    public static final class VolumeSmaComparator implements Comparator<Histogram> {

        @Override
        public int compare(Histogram o1, Histogram o2) {
            return Long.compare(o1.volumeSma, o2.volumeSma);
        }
    }

    public static final class PriceComparator implements Comparator<Histogram> {

        @Override
        public int compare(Histogram o1, Histogram o2) {
            return Double.compare(o1.high, o2.high);
        }
    }

}
