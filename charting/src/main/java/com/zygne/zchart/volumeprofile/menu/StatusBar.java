package com.zygne.zchart.volumeprofile.menu;

import com.zygne.zchart.volumeprofile.model.chart.BoxContainer;
import com.zygne.zchart.volumeprofile.model.chart.Canvas;
import com.zygne.zchart.volumeprofile.model.chart.TextObject;

public class StatusBar extends BoxContainer {

    private int labelWidth = 10;
    private int statusBarHeight = 25;
    private long highestValue;
    private double grouping;

    @Override
    public void init() {

    }

    @Override
    public void draw(Canvas canvas) {
        int canvasWidth = width;
        int canvasHeight = height;
        int statusBarTop = canvasHeight - (statusBarHeight);
        int statusBarMid = canvasHeight - (statusBarHeight / 2);

        canvas.setColor("#000000");
        canvas.drawRectangle(0, statusBarTop, canvasWidth, statusBarHeight, Canvas.Fill.SOLID);

        canvas.setColor("#FFFFFF");

        TextObject statusText = new TextObject();
        statusText.setX(0);
        statusText.setY(statusBarMid);
        statusText.setText("G = " + String.format("%.2f", grouping) + " | Volume");
        statusText.setAlignment(TextObject.Alignment.LEFT);
        statusText.draw(canvas);

        canvas.drawLine(0, statusBarTop, canvasWidth, statusBarTop, Canvas.LineStyle.SOLID);


        /*
        int chartWith = canvasWidth + labelWidth;

        double increment = 0.25;
        double scalar = 0.25;

        int index = 0;
        while (index < 3){
            TextObject textObject = new TextObject();
            textObject.setX((int) (chartWith * scalar));
            textObject.setY(statusBarMid);
            textObject.setText(String.format("%,d", (long) (highestValue * scalar)));

            textObject.draw(canvas);

            canvas.setColor("#C4C4C4");
            canvas.drawLine((int) (chartWith * scalar), 0, (int) (chartWith * scalar), statusBarTop, Canvas.LineStyle.DASHED);

            index++;

            scalar += increment;
        }
         */

    }

    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    public void setHighestValue(long highestValue) {
        this.highestValue = highestValue;
    }

    public void setGrouping(double grouping) {
        this.grouping = grouping;
    }
}
