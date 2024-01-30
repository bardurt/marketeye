package com.zygne.chart.chart.model.data;

import com.zygne.chart.chart.model.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;

import java.util.Comparator;

public class PointAndFigure extends Object2d {

    public static final int BULLISH_BOX = 1;
    public static final int BEARISH_BOX = 2;

    private int state = BULLISH_BOX;
    private int column = 0;
    private double price;
    private int boxCount;
    private double boxSize;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }

    public double getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(double boxSize) {
        this.boxSize = boxSize;
    }

    @Override
    public void draw(Canvas canvas) {
        if (state == BULLISH_BOX) {
            canvas.setColor("#00DE00");
        } else {
            canvas.setColor("#DE0000");
        }

        canvas.drawRectangle(getLeft(), getTop(), getWidth(), getHeight(), Canvas.Fill.SOLID);

        canvas.setColor("#FFFDFD");
        canvas.drawRectangle(getLeft(), getTop(), getWidth(), getHeight(), Canvas.Fill.OUTLINE);

    }

    public static final class PriceComparator implements Comparator<PointAndFigure> {

        @Override
        public int compare(PointAndFigure o1, PointAndFigure o2) {
            return Double.compare(o1.price, o2.price);
        }
    }
}
