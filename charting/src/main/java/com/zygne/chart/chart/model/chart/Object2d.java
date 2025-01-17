package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Renderable;

public abstract class Object2d extends BoxCollider implements Renderable {

    protected ColorSchema colorSchema = ColorSchema.BLUE;
    protected String color = Colors.WHITE;
    private int zOrder = 0;

    public void setColorSchema(ColorSchema colorSchema) {
        this.colorSchema = colorSchema;

        switch (colorSchema) {
            case WHITE -> color = Colors.SNOW_WHITE;
            case RED -> color = Colors.RED;
            case ORANGE -> color = Colors.ORANGE;
            case YELLOW -> color = Colors.YELLOW;
            case BLUE -> color = Colors.BLUE;
            case GREEN -> color = Colors.GREEN;
            case HIGHLIGHT -> color = Colors.WHITE;
            case BLUE_METAL -> color = Colors.BLUE_METAL;
        }
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
        HIGHLIGHT,
        GRAY,
        BLUE_METAL
    }
}
