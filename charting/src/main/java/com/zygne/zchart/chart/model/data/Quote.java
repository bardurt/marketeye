package com.zygne.zchart.chart.model.data;

import java.util.Comparator;

public class Quote {

    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private long timeStamp;
    private double percentile;
    private boolean currentPrice;
    private int index;
    private long volumeSma;
    private double volumeSmaPercentile;

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

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

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    public boolean isCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(boolean currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getRange(){
        return high - low;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getVolumeSma() {
        return volumeSma;
    }

    public void setVolumeSma(long volumeSma) {
        this.volumeSma = volumeSma;
    }

    public double getVolumeSmaPercentile() {
        return volumeSmaPercentile;
    }

    public void setVolumeSmaPercentile(double volumeSmaPercentile) {
        this.volumeSmaPercentile = volumeSmaPercentile;
    }

    public static final class VolumeComparator implements Comparator<Quote> {

        @Override
        public int compare(Quote o1, Quote o2) {
            return Long.compare(o1.volume, o2.volume);
        }
    }

    public static final class PriceComparator implements Comparator<Quote> {

        public static final int SORT_ORDER_OPEN = 1;
        public static final int SORT_ORDER_HIGH = 2;
        public static final int SORT_ORDER_LOW = 3;
        public static final int SORT_ORDER_CLOSE = 4;

        private int sortOrder = SORT_ORDER_CLOSE;

        public PriceComparator() {
        }

        public PriceComparator(int sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(Quote o1, Quote o2) {

            if(sortOrder == SORT_ORDER_OPEN){
                return Double.compare(o1.getOpen(), o2.getOpen());
            } else if (sortOrder == SORT_ORDER_HIGH){
                return Double.compare(o1.getHigh(), o2.getHigh());
            } else if(sortOrder == SORT_ORDER_LOW){
                return Double.compare(o1.getLow(), o2.getLow());
            } else {
                return Double.compare(o1.getClose(), o2.getClose());
            }
        }
    }

    public static final class TimeComparator implements Comparator<Quote> {

        @Override
        public int compare(Quote o1, Quote o2) {
            return Long.compare(o1.getTimeStamp(), o2.getTimeStamp());
        }
    }

    @Override
    public String toString() {
        return "O " + open + ",  H " + high + ", L " + low + ", C " + close;
    }
}
