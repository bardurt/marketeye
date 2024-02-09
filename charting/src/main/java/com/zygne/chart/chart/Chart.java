package com.zygne.chart.chart;

import com.zygne.chart.chart.model.data.Serie;

import java.util.List;

public interface Chart {
    public void draw(Canvas g);

    public void setWaterMark(String waterMark);

    public void setTitle(String title);

    public void setSeries(List<List<Serie>> series);
}
