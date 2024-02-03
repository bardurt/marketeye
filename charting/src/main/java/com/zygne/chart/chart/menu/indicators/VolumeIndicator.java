package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.VolumeBar;

import java.util.List;

public class VolumeIndicator extends Object2d {

    private List<VolumeBar> volumeBarList;

    public VolumeIndicator(List<VolumeBar> volumeBarList) {
        this.volumeBarList = volumeBarList;
    }

    @Override
    public void draw(Canvas canvas) {
        for (VolumeBar e : volumeBarList) {
            e.draw(canvas);
        }
    }
}
