package com.zygne.zchart.chart.model.chart;

public class VolumeBubble extends Object2d {

    private double percentile;

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(255, 231, 147,  128);
        canvas.drawCircle(x, y-height, width, height, Canvas.Fill.SOLID);
    }
}
