package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.model.data.PriceBox;

public class CandleStick extends Object2d {

    private double high;
    private double low;
    private double open;
    private double close;
    private double scalar;
    private long id;
    private PriceBox priceBox;
    private long volume;
    private long timeStamp;
    private long volumeSma;
    private double volumeSmaPercentile;

    private int bodyTop;
    private int bodyHeight;
    private String color;

    public void ohlc(double open, double high, double low, double close, long volume, double scalar) {
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.priceBox = new PriceBox();
        this.priceBox.setEnd(high*scalar);
        this.priceBox.setStart(low*scalar);
        this.volume = volume;
        this.scalar = scalar;


        int height = (int) ((high - low) * scalar);
        this.setHeight(height);
        this.setY((int) (high * scalar));

        if (open < close) {
            bodyTop = (int) (-close * scalar);
            bodyHeight = (int) ((close - open) * scalar);

            color = "#0093FF";
        } else {
            bodyTop = (int) (-open * scalar);
            bodyHeight = (int) ((open - close) * scalar);

            color = "#FF7800";
        }

        if (bodyHeight < 1) {
            bodyHeight = 1;
        }
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