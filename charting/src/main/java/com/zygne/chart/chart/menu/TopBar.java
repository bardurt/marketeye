package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.model.chart.BoxContainer;
import com.zygne.chart.chart.model.chart.Canvas;
import com.zygne.chart.chart.model.chart.TextObject;

public class TopBar extends BoxContainer {

    private int statusBarHeight = 25;
    private TextObject statusText;
    private TextObject labelText;

    @Override
    public void init() {
        statusText = new TextObject(100, 0, 100, 20);
        statusText.setAlignment(TextObject.Alignment.LEFT);
        statusText.setFontSize(TextObject.FontSize.SMALL);
        statusText.setzOrder(-1);

        labelText = new TextObject(10, 0, 1, 20);
        labelText.setAlignment(TextObject.Alignment.LEFT);
        labelText.setFontSize(TextObject.FontSize.SMALL);
        labelText.setzOrder(-1);
    }

    @Override
    public void draw(Canvas canvas) {
        int canvasWidth = width;

        canvas.setColor("#000000");
        canvas.drawRectangle(0, 0, canvasWidth, statusBarHeight, Canvas.Fill.SOLID);

        canvas.setColor("#FFFFFF");
        canvas.drawLine(0, statusBarHeight, canvasWidth, statusBarHeight, Canvas.LineStyle.SOLID);

        statusText.draw(canvas);
        labelText.draw(canvas);
    }

    public void setStatusText(String statusText){
        this.statusText.setText(statusText);
    }

    public void setLabelText(String labelText){
        this.labelText.setText(labelText);
    }
}
