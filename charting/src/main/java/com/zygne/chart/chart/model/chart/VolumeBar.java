package com.zygne.chart.chart.model.chart;

public class VolumeBar extends Object2d {

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(15, 151, 255,  128);
        canvas.drawRectangle(x, y-height, width, height, Canvas.Fill.SOLID);
    }

    @Override
    public void setColorSchema(Object2d.ColorSchema colorSchema) {


    }

}