package com.zygne.stockanalyzer.domain.model;

import java.util.Comparator;

public class PriceGap {

    private double outerStart;
    private double outerEnd;
    private double innerStart;
    private double innerEnd;
    private long timeStamp;
    private int index;
    private boolean filled;

    public double getOuterStart() {
        return outerStart;
    }

    public void setOuterStart(double outerStart) {
        this.outerStart = outerStart;
    }

    public double getOuterEnd() {
        return outerEnd;
    }

    public void setOuterEnd(double outerEnd) {
        this.outerEnd = outerEnd;
    }

    public double getInnerStart() {
        return innerStart;
    }

    public void setInnerStart(double innerStart) {
        this.innerStart = innerStart;
    }

    public double getInnerEnd() {
        return innerEnd;
    }

    public void setInnerEnd(double innerEnd) {
        this.innerEnd = innerEnd;
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
        return (innerStart + innerEnd)/2;
    }

    public static final class TimeComparator implements Comparator<PriceGap> {

        @Override
        public int compare(PriceGap o1, PriceGap o2) {
            return Long.compare(o1.timeStamp, o2.timeStamp);
        }
    }
}
