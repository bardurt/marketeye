package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class VolumeBar extends Object2d {

    @Override
    public void draw(Canvas canvas) {

        if (colorSchema == ColorSchema.BLUE) {
            canvas.setColor(Colors.BLUE);
        } else {
            canvas.setColor(Colors.YELLOW);
        }

        canvas.drawRectangle(x, y - height, width, height, Canvas.Fill.SOLID);
    }
}