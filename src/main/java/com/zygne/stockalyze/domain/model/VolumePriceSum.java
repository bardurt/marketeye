package com.zygne.stockalyze.domain.model;

public class VolumePriceSum {

    public final double price;
    public long totalSize;

    public VolumePriceSum(double price, long totalSize) {
        this.price = price;
        this.totalSize = totalSize;
    }
}
