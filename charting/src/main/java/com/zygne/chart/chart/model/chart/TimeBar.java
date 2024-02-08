package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class TimeBar extends Object2d {

    private final TextObject textObject = new TextObject();

    public void setText(String text) {
        this.textObject.setFontSize(TextObject.FontSize.SMALL_EXTRA);
        this.textObject.setText(text);
        this.textObject.setAlignment(TextObject.Alignment.LEFT);
    }

    public void setText(String text, TextObject.FontSize fontSize) {
        this.textObject.setFontSize(fontSize);
        this.textObject.setText(text);
        this.textObject.setAlignment(TextObject.Alignment.LEFT);
    }

    @Override
    public void draw(Canvas canvas) {
        textObject.setX(x);
        textObject.setY(y);
        textObject.setWidth(width);
        textObject.setHeight(height);
        textObject.draw(canvas);
    }
}
