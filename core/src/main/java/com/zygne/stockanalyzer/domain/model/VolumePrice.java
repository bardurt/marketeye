package com.zygne.stockanalyzer.domain.model;

public class VolumePrice {

    private final double price;
    private final long size;

    public VolumePrice(double price, long size) {
        this.size = size;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public long getSize() {
        return size;
    }
}
