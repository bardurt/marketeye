package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.model.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.PriceImbalance;

import java.util.ArrayList;
import java.util.List;

public class PriceImbalanceIndicator extends Object2d {

    private int padding = 0;
    private int increment = 20;
    private List<PriceImbalance> imbalances = new ArrayList<>();

    public PriceImbalanceIndicator(List<PriceImbalance> imbalances) {
        this.imbalances = imbalances;
    }

    public void increase() {
        padding+= 20;
    }

    public void decrease() {
        padding -= 20;
        if(padding <=0){
            padding = 0;
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
