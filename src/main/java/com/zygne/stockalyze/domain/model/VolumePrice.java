package com.zygne.stockalyze.domain.model;

public class VolumePrice {

    public final double price;
    public final long size;

    public VolumePrice(double price, long size) {
        this.size = size;
        this.price = price;
    }
}
