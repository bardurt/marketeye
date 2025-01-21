package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class VolumeProfileLine extends Object2d {

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(color);
        canvas.drawRectangle(x, y, width, height, Canvas.Fill.SOLID);
    }
}