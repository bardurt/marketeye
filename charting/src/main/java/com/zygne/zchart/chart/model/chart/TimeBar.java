package com.zygne.zchart.chart.model.chart;

public class TimeBar extends Object2d {

    private TextObject textObject = new TextObject();

    public void setText(String text) {
        this.textObject.setFontSize(TextObject.FontSize.SMALL_EXTRA);
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
