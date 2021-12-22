package com.zygne.zchart.chart.menu.indicators;

import com.zygne.zchart.chart.model.chart.Canvas;
import com.zygne.zchart.chart.model.chart.Object2d;
import com.zygne.zchart.chart.model.chart.PriceImbalance;

import java.util.ArrayList;
import java.util.List;

public class PriceImbalanceIndicator extends Object2d {

    private int padding = 0;
    private List<PriceImbalance> imbalances = new ArrayList<>();

    public PriceImbalanceIndicator(List<PriceImbalance> imbalances) {
        this.imbalances = imbalances;
    }


    public void increase() {
        padding++;
        if (padding > 100) {
            padding = 100;
        }
    }

    public void decrease() {
        padding++;
        if (padding > 100) {
            padding = 100;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        for (PriceImbalance e : imbalances) {
            e.setPaddingLeft(padding);
            e.draw(canvas);
        }
    }
}
