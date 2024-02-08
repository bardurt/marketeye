package com.zygne.chart.chart.model.data;

public abstract class LineItem extends Serie {
    protected double x = 0d;
    protected double y = 0d;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
