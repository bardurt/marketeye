package com.zygne.zchart.chart.menu.indicators;

import com.zygne.zchart.chart.model.chart.Canvas;
import com.zygne.zchart.chart.model.chart.Object2d;
import com.zygne.zchart.chart.model.chart.PricePressure;

import java.util.ArrayList;
import java.util.List;

public class PricePressureIndicator extends Object2d {


    private double minPercentile;
    private double percentile;
    private List<PricePressure> pricePressures = new ArrayList<>();

    public PricePressureIndicator(List<PricePressure> pricePressures, double minPercentile) {
        this.pricePressures = pricePressures;
        this.minPercentile = minPercentile;
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
        for (PricePressure e : pricePressures) {
            if (e.getPercentile() >= percentile) {
                e.draw(canvas);
            }
        }
    }


}