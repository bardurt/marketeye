package com.zygne.chart.chart.model.data;

import java.util.Comparator;

public class CandleSerie extends QuoteSerie {

    private double percentile;
    private long volumeSma;
    private double volumeSmaPercentile;
    private double priceSma;

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
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

    public double getPriceSma() {
        return priceSma;
    }

    public void setPriceSma(double priceSma) {
        this.priceSma = priceSma;
    }

    public static final class VolumeComparator implements Comparator<CandleSerie> {

        @Override
        public int compare(CandleSerie o1, CandleSerie o2) {
            return Long.compare(o1.volume, o2.volume);
        }
    }

    public static final class PriceComparator implements Comparator<CandleSerie> {

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
        public int compare(CandleSerie o1, CandleSerie o2) {

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

    public static final class TimeComparator implements Comparator<CandleSerie> {

        @Override
        public int compare(CandleSerie o1, CandleSerie o2) {
            return Long.compare(o1.getTimeStamp(), o2.getTimeStamp());
        }
    }

    @Override
    public String toString() {
        return "O " + open + ",  H " + high + ", L " + low + ", C " + close;
    }
}
