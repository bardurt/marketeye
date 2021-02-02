package com.zygne.stockanalyzer.domain.model;

import java.util.Comparator;

public class PriceGap {

    private double start;
    private double end;
    private long timeStamp;
    private int index;
    private boolean filled;

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public double getMidPoint(){
        return (start + end)/2;
    }

    public static final class TimeComparator implements Comparator<PriceGap> {

        @Override
        public int compare(PriceGap o1, PriceGap o2) {
            return Long.compare(o1.timeStamp, o2.timeStamp);
        }
    }
}
