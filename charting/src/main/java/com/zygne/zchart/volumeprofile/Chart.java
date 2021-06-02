package com.zygne.zchart.volumeprofile;

import com.zygne.zchart.volumeprofile.model.chart.Canvas;
import com.zygne.zchart.volumeprofile.model.data.Quote;

import java.awt.*;
import java.util.List;

public interface Chart {
    public void draw(Canvas g);
    public void setQuotes(List<Quote> quotes);
}
