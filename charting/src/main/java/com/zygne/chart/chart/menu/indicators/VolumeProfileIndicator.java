package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.VolumeProfileLine;

import java.util.List;

public class VolumeProfileIndicator extends Object2d {

    private List<VolumeProfileLine> volumeProfileLines;

    public VolumeProfileIndicator(List<VolumeProfileLine> volumeProfileLines) {
        this.volumeProfileLines = volumeProfileLines;
    }

    @Override
    public void draw(Canvas canvas) {

        int i = 0;
        for(VolumeProfileLine e : volumeProfileLines){

            i++;
            e.draw(canvas);
        }
    }
}
