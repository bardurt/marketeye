package com.zygne.chart.chart.model.chart;

public class PriceGap extends Object2d {

    private ColorSchema initialColorSchema = ColorSchema.GREEN;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(39, 200, 47, 128);
        canvas.drawRectangle(x, y, width, height, Canvas.Fill.SOLID);
    }

    @Override
    public void setColorSchema(ColorSchema colorSchema) {
        this.initialColorSchema = colorSchema;
    }

}