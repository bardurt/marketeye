package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class PricePressure extends Object2d {


    private ColorSchema initialColorSchema = ColorSchema.YELLOW;
    private ColorSchema colorSchema = ColorSchema.YELLOW;

    private double price;
    private long id;
    private double percentile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(0, 180, 193, 64);
        canvas.drawRectangle(x, y-1, width, height+1, Canvas.Fill.SOLID);

        canvas.setColor(0, 180, 193,  64);
        canvas.drawRectangle(x, y, width, height, Canvas.Fill.SOLID);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    @Override
    public void setColorSchema(ColorSchema colorSchema) {
        this.initialColorSchema = colorSchema;
        this.colorSchema = initialColorSchema;
    }

}