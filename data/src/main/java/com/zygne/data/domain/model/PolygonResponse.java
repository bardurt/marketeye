package com.zygne.data.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PolygonResponse {

    @SerializedName("ticker")
    public String ticker;

    @SerializedName("results")
    public List<PolygonData> data;
}
