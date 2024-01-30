package com.zygne.data.domain.model;

public class VolumePriceSum {

    public final double price;
    public long totalSize;
    public int hits;

    public VolumePriceSum(double price, long totalSize) {
        this.price = price;
        this.totalSize = totalSize;
    }
}
