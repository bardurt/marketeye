package com.zygne.chart.chart;

import com.zygne.chart.chart.model.data.Quote;

import java.util.List;

public interface Chart  {
    void draw(Canvas g);
    void setWaterMark(String waterMark);
    void setTitle(String title);
    void setSeries(List<List<Quote>> series);
    void setSeriesName(List<String> names);
    void setCurrentPrice(double price);
}
