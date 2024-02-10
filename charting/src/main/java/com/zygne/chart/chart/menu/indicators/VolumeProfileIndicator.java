package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.VolumeProfileLine;

import java.util.List;

public class VolumeProfileIndicator extends Object2d {

    private final List<VolumeProfileLine> volumeProfileLines;

    public VolumeProfileIndicator(List<VolumeProfileLine> volumeProfileLines) {
        this.volumeProfileLines = volumeProfileLines;
    }

    @Override
    public void draw(Canvas canvas) {
        for(VolumeProfileLine e : volumeProfileLines){
            e.draw(canvas);
        }
    }
}
