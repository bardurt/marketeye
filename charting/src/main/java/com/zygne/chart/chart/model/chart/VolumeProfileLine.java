package com.zygne.chart.chart.model.chart;

public class VolumeProfileLine extends Object2d {

    private ColorSchema initialColorSchema = ColorSchema.BLUE;
    private ColorSchema colorSchema = ColorSchema.BLUE;
    private String boxColorHex;

    @Override
    public void draw(Canvas canvas) {

        prepareColors();

        canvas.setColor(boxColorHex);
        canvas.drawRectangle(x, y, width, height, Canvas.Fill.SOLID);

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