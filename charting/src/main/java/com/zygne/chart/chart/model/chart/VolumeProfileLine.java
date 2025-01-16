package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class VolumeProfileLine extends Object2d {

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
            boxColorHex = Colors.BLUE;
        } else if (colorSchema == ColorSchema.RED) {
            boxColorHex = Colors.RED;
        } else if (colorSchema == ColorSchema.ORANGE) {
            boxColorHex = Colors.ORANGE;
        } else if (colorSchema == ColorSchema.YELLOW) {
            boxColorHex = Colors.YELLOW;
        } else if (colorSchema == ColorSchema.GREEN) {
            boxColorHex = Colors.GREEN;
        } else if (colorSchema == ColorSchema.HIGHLIGHT) {
            boxColorHex = Colors.WHITE;
        } else {
            boxColorHex = Colors.BLUE;
        }
    }

    @Override
    public void setColorSchema(ColorSchema colorSchema) {
        this.colorSchema = colorSchema;
    }
}