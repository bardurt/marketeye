package com.zygne.stockalyze.domain.model;

public class VolumePriceGroup {

    public final double price;
    public long totalSize;
    public int orderCount;

    public VolumePriceGroup(double price, long totalSize) {
        this.price = price;
        this.totalSize = totalSize;
        this.orderCount = 1;
    }
}
