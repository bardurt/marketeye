package com.zygne.chart.chart;

import com.zygne.chart.chart.model.chart.Canvas;
import com.zygne.chart.chart.model.data.Quote;

import java.util.List;

public interface Chart  {
    public void draw(Canvas g);
    public void setBars(List<Quote> bars);
    public void setWaterMark(String waterMark);
    public void setTitle(String title);
    public void setCurrentPrice(double price);
}
