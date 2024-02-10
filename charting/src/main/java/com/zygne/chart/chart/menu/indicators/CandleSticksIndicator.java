package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;

import java.util.List;

public class CandleSticksIndicator extends Object2d {

    private final List<CandleStick> candleSticks;

    public CandleSticksIndicator(List<CandleStick> candleSticks) {
        this.candleSticks = candleSticks;
    }

    public List<CandleStick> getCandleSticks() {
        return candleSticks;
    }

    @Override
    public void draw(Canvas canvas) {
        for (CandleStick e : candleSticks) {
            e.draw(canvas);
        }
    }
}
