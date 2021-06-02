package com.zygne.zchart.volumeprofile.model.chart;

import com.zygne.zchart.volumeprofile.model.data.PriceBox;


public class PriceLine extends Object2d {

    private TextObject textObject = new TextObject();
    private PriceBox priceBox = new PriceBox();

    public PriceLine() {
        textObject.setText("");
        textObject.setFontSize(TextObject.FontSize.SMALL_EXTRA);
        textObject.setAlignment(TextObject.Alignment.LEFT);
    }

    public PriceBox getPriceBox() {
        return priceBox;
    }

    @Override
    public void draw(Canvas canvas) {
        textObject.setX(x+2);
        textObject.setY(y);
        textObject.setWidth(50);
        textObject.setHeight(10);
        textObject.setText(String.format("%.2f",priceBox.getStart()));

        textObject.draw(canvas);

        canvas.setColor("#ABABAB");
        canvas.drawLine(getLeft(), getBottom(), getRight(), getBottom(), Canvas.LineStyle.SOLID);

    }
}
