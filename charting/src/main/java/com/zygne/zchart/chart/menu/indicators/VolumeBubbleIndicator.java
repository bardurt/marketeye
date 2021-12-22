package com.zygne.zchart.chart.menu.indicators;

import com.zygne.zchart.chart.model.chart.Canvas;
import com.zygne.zchart.chart.model.chart.Object2d;
import com.zygne.zchart.chart.model.chart.VolumeBubble;

import java.util.List;

public class VolumeBubbleIndicator extends Object2d {

    private double minPercentile;
    private double percentile;
    private List<VolumeBubble> volumeBubbleList;

    public VolumeBubbleIndicator(List<VolumeBubble> volumeBubbleList, double percentile) {
        this.volumeBubbleList = volumeBubbleList;
        this.minPercentile = percentile;
    }

    public void increase() {
        percentile++;
        if (percentile > 99) {
            percentile = 99;
        }
    }

    public void decrease() {
        percentile--;
        if (percentile < minPercentile) {
            percentile = minPercentile;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for(VolumeBubble e : volumeBubbleList){
            if(e.getPercentile() >= percentile) {
                e.draw(canvas);
            }
        }
    }
}
