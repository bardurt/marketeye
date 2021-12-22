package com.zygne.zchart.chart.menu.indicators;

import com.zygne.zchart.chart.model.chart.Canvas;
import com.zygne.zchart.chart.model.chart.Object2d;
import com.zygne.zchart.chart.model.chart.TimeBar;

import java.util.List;

public class TimeIndicator extends Object2d {

    private List<TimeBar> timeBarList;

    public TimeIndicator(List<TimeBar> timeBarList) {
        this.timeBarList = timeBarList;
    }

    @Override
    public void draw(Canvas canvas) {
        for (TimeBar e : timeBarList) {
            e.draw(canvas);
        }
    }
}
