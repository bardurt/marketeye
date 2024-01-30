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


    public static boolean isSameHour(Histogram h1, Histogram h2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(h1.timeStamp);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(h2.timeStamp);


        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
            if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
                if (calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isSameDay(Histogram h1, Histogram h2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(h1.timeStamp);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(h2.timeStamp);


        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
            if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
                return true;
            }
        }

        return false;
    }


    public Histogram deepCopy() {
        Histogram clone = new Histogram();

        clone.dateTime = this.dateTime;
        clone.timeStamp = this.timeStamp;
        clone.open = this.open;
        clone.high = this.high;
        clone.low = this.low;
        clone.close = this.close;
        clone.volume = this.volume;
        clone.volumeSma = this.volumeSma;
        clone.volumeSmaPercentile = this.volumeSmaPercentile;

        return clone;
    }
}
