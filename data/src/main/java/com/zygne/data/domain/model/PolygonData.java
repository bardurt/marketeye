package com.zygne.data.domain.model;

import com.google.gson.annotations.SerializedName;

public class PolygonData {


    @SerializedName("o")
    public double open;

    @SerializedName("h")
    public double high;

    @SerializedName("l")
    public double low;

    @SerializedName("c")
    public double close;

    @SerializedName("n")
    public double volume;

    @SerializedName("t")
    public long timeStamp;
}
