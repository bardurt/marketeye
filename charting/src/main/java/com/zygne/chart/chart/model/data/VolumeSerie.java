package com.zygne.chart.chart.model.data;

import java.util.Comparator;

public class VolumeSerie extends Serie {

    private long volume;
    private double price;

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static final class PriceComparator implements Comparator<VolumeSerie> {

        public PriceComparator() {
        }

        @Override
        public int compare(VolumeSerie o1, VolumeSerie o2) {
            return Double.compare(o1.getPrice(), o2.getPrice());
        }

    }
}
