package com.zygne.data.domain.model;


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

    public boolean isSameWeek(long timeStamp) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(this.timeStamp);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(timeStamp);

        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }

        return false;
    }

    public boolean isSameMonth(long timeStamp) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(this.timeStamp);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(timeStamp);

        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                return true;
            }
        }

        return false;
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
