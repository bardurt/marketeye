package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Renderable;

public abstract class Object2d extends BoxCollider implements Renderable {

    protected ColorSchema colorSchema = ColorSchema.BLUE;
    private int zOrder = 0;

    public void setColorSchema(ColorSchema colorSchema) {
        this.colorSchema = colorSchema;
    }

    public ColorSchema getColorSchema() {
        return colorSchema;
    }

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
        WHITE,
        HIGHLIGHT
    }
}
