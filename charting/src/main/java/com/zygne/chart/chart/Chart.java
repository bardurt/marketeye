package com.zygne.chart.chart;

import com.zygne.chart.chart.model.data.Quote;

import java.util.List;

public interface Chart  {
    void draw(Canvas g);
    void setBars(List<Quote> bars);
    void setWaterMark(String waterMark);
    void setTitle(String title);
    void setCurrentPrice(double price);
}
