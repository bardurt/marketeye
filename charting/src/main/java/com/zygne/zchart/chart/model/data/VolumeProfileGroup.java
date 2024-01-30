package com.zygne.zchart.chart.model.data;

import java.util.Comparator;

public class VolumeProfileGroup {

    private PriceBox priceBox;
    private long volume;

    public double getPrice() {
        return priceBox.getStart();
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public PriceBox getPriceBox() {
        return priceBox;
    }

    public void setPriceBox(PriceBox priceBox) {
        this.priceBox = priceBox;
    }

    public static final class VolumeComparator implements Comparator<VolumeProfileGroup> {

        @Override
        public int compare(VolumeProfileGroup o1, VolumeProfileGroup o2) {
            return Long.compare(o1.volume, o2.volume);
        }
    }

    public static final class PriceComparator implements Comparator<VolumeProfileGroup> {

        @Override
        public int compare(VolumeProfileGroup o1, VolumeProfileGroup o2) {
            return Double.compare(o1.getPrice(), o2.getPrice());
        }
    }
}
