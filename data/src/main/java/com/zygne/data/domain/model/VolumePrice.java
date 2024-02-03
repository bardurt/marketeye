package com.zygne.data.domain.model;

public record VolumePrice(double price, long size) {

    public double getPrice() {
        return price;
    }

    public long getSize() {
        return size;
    }
}
