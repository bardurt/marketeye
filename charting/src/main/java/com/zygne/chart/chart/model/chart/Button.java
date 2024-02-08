package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class Button extends Object2d {

    private final TextObject text;

    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        text = new TextObject(x, y, width, height);
        text.setText("Button");
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public void draw(Canvas canvas) {
        int posX = x;
        int posY = y;
        int boxWith = width;
        int boxHeight = height;

        canvas.setColor("#00A8E1");
        canvas.drawRectangle(posX, posY, boxWith, boxHeight, Canvas.Fill.SOLID);
        canvas.setColor("#ABABAB");
        canvas.drawRectangle(posX, posY, boxWith, boxHeight, Canvas.Fill.OUTLINE);

        canvas.setColor("#FFFFFF");
        canvas.drawLine(posX, posY, posX + boxWith, posY, Canvas.LineStyle.SOLID, Canvas.LineWidth.SMALL);
        canvas.drawLine(posX, posY, posX, posY + boxHeight, Canvas.LineStyle.SOLID, Canvas.LineWidth.SMALL);

        canvas.setColor("#000000");

        text.draw(canvas);
    }
}
