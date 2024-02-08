package com.zygne.chart.chart;

import com.zygne.chart.chart.model.data.Serie;

import java.util.List;

public interface Chart  {
    void draw(Canvas g);
    void setWaterMark(String waterMark);
    void setTitle(String title);
    void setSeries(List<List<Serie>> series);
}
