package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.data.PriceBox;

public class CandleStick extends Object2d {

    private long id;
    private PriceBox priceBox;
    private long volume;
    private long timeStamp;
    private long volumeSma;
    private double volumeSmaPercentile;

    private int bodyTop;
    private int bodyHeight;
    private String color;
    public boolean visible;
    private double open;
    private double high;
    private double low;
    private double close;

    public void ohlc(double open, double high, double low, double close, long volume, double scalar) {
        this.open = open;
        this.high = high;
        this.open = open;
        this.low = low;
        this.close = close;
        this.priceBox = new PriceBox();
        this.priceBox.setEnd(high);
        this.priceBox.setStart(low);
        this.volume = volume;

        setUp(scalar);
    }

    private void setUp(double scalar){
        int height = (int) ((this.high - this.low) * scalar);
        this.setHeight(height);
        this.setY((int) (this.high * scalar));

        if (this.open < this.close) {
            bodyTop = (int) (-this.close * scalar);
            bodyHeight = (int) ((this.close - this.open) * scalar);
            color = Colors.BLUE;
        } else {
            bodyTop = (int) (-this.open * scalar);
            bodyHeight = (int) ((this.open - this.close) * scalar);
            color = Colors.ORANGE_BRIGHT;
        }

        if (bodyHeight < 1) {
            bodyHeight = 1;
        }
    }

    public void adjust(double scalar) {
        setUp(scalar);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getVolumeSma() {
        return volumeSma;
    }

    public void setVolumeSma(long volumeSma) {
        this.volumeSma = volumeSma;
    }

    public double getVolumeSmaPercentile() {
        return volumeSmaPercentile;
    }

    public void setVolumeSmaPercentile(double volumeSmaPercentile) {
        this.volumeSmaPercentile = volumeSmaPercentile;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(color);
        canvas.drawRectangle(x, bodyTop, width, bodyHeight, Canvas.Fill.SOLID);
        canvas.drawRectangle(x + width / 2, -y, 1, height, Canvas.Fill.SOLID);
    }

    public PriceBox getPriceBox() {
        return priceBox;
    }

    public long getVolume() {
        return volume;
    }

    @Override
    public void setColorSchema(ColorSchema colorSchema) {

    }
}