package com.zygne.chart.chart.model.chart;

public class PriceImbalance  extends Object2d {

    private ColorSchema initialColorSchema = ColorSchema.YELLOW;
    private ColorSchema colorSchema = ColorSchema.YELLOW;
    private String boxColorHex;
    private int paddingLeft;

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    @Override
    public void draw(Canvas canvas) {

        prepareColors();

        canvas.setColor(205, 193, 29, 100);
        canvas.drawRectangle(x, y, width+paddingLeft, height, Canvas.Fill.SOLID);

    }

    private void prepareColors() {
        if (colorSchema == ColorSchema.BLUE) {
            boxColorHex = "#0093FF";
        } else if (colorSchema == ColorSchema.RED) {
            boxColorHex = "#FF0000";
        } else if (colorSchema == ColorSchema.ORANGE) {
            boxColorHex = "#FF9E00";
        } else if (colorSchema == ColorSchema.YELLOW) {
            boxColorHex = "#FFE800";
        } else if (colorSchema == ColorSchema.GREEN) {
            boxColorHex = "#23FF00";
        } else if (colorSchema == ColorSchema.HIGHLIGHT) {
            boxColorHex = "#FFFFFF";
        }

    }


    @Override
    public void setColorSchema(ColorSchema colorSchema) {
        this.initialColorSchema = colorSchema;
        this.colorSchema = initialColorSchema;
    }

}