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
        canvas.setColor(color);
        canvas.drawRectangle(x, y, width, height, Canvas.Fill.SOLID);
    }
}
