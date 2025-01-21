package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class VolumeBar extends Object2d {

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(color);
        canvas.drawRectangle(x, y - height, width, height, Canvas.Fill.SOLID);
    }
}