package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class Bar extends Object2d{

    private long timeStamp;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public void draw(Canvas canvas) {
        switch (colorSchema){

            case RED -> canvas.setColor(Colors.RED);
            case ORANGE, HIGHLIGHT, WHITE, BLUE, YELLOW -> {
            }
            case GREEN -> canvas.setColor(Colors.GREEN);
            default -> {}
        }
        canvas.drawRectangle(x, y, width, height, Canvas.Fill.SOLID);
    }
}
