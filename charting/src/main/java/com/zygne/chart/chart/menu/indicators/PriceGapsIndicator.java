package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.PriceGap;

import java.util.List;

public class PriceGapsIndicator extends Object2d {

    private List<PriceGap> priceGaps;

    public PriceGapsIndicator(List<PriceGap> priceGaps) {
        this.priceGaps = priceGaps;
    }

    @Override
    public void draw(Canvas canvas) {
        for (PriceGap e : priceGaps) {
            e.draw(canvas);
        }
    }
}
