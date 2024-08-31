package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class Bar extends Object2d{

    private long timeStamp;
    private boolean showMonthOnly = false;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean getShowMonthOnly() {
        return showMonthOnly;
    }

    public void setShowMonthOnly(boolean showMonthOnly) {
        this.showMonthOnly = showMonthOnly;
    }

    @Override
    public void draw(Canvas canvas) {
        switch (colorSchema){

            case RED -> canvas.setColor(Colors.RED);
            case GRAY -> canvas.setColor(Colors.SILVER);
            case BLUE -> canvas.setColor(Colors.BLUE);
            case ORANGE, HIGHLIGHT, WHITE, YELLOW -> {
            }
            case GREEN -> canvas.setColor(Colors.GREEN);
            default -> {}
        }
        canvas.drawRectangle(x, y, width, height, Canvas.Fill.SOLID);
    }
}
