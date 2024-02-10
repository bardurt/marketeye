package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Bar;
import com.zygne.chart.chart.model.chart.Object2d;

import java.util.ArrayList;
import java.util.List;

public class BarIndicator extends Object2d {

    private final List<Bar> bars = new ArrayList<>();

    public void addBar(Bar b) {
        this.bars.add(b);
    }

    public List<Bar> getBars(){
        return bars;
    }

    @Override
    public void draw(Canvas canvas) {
        for (Bar b : bars) {
            b.draw(canvas);
        }
    }
}