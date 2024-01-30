package com.zygne.chart.chart.model.chart;

public class OriginPoint extends Object2d {
    @Override
    public void draw(Canvas canvas) {
        canvas.setColor("#FFFFFF");
        canvas.drawLine(getLeft(), getBottom(), getRight(), getBottom(), Canvas.LineStyle.SOLID);
    }
}
