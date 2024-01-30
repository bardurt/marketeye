package com.zygne.chart.chart.model.chart;

public abstract class Object2d extends BoxCollider implements Renderable{

    private int zOrder = 0;

    public void setColorSchema(VolumeProfileBar.ColorSchema colorSchema){}

    public int getzOrder() {
        return zOrder;
    }

    public void setzOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public enum ColorSchema {
        RED,
        ORANGE,
        YELLOW,
        BLUE,
        GREEN,
        HIGHLIGHT
    }

}
