package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.model.chart.BoxContainer;
import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.TextObject;

public class StatusBar extends BoxContainer {

    private int labelWidth = 30;
    private int statusBarHeight = 25;
    private long highestValue;

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


        canvas.drawLine(0, statusBarTop, canvasWidth, statusBarTop, Canvas.LineStyle.SOLID, Canvas.LineWidth.SMALL);

        TextObject label = new TextObject();
        label.setX(5);
        label.setAlignment(TextObject.Alignment.LEFT);
        label.setY(statusBarMid);
        label.setText("Vol :");
        label.draw(canvas);

        int chartWith = canvasWidth;

        double increment = 0.25;
        double scalar = 0.25;

        int index = 0;

        canvas.setColor("#C4C4C4");
        canvas.drawLine(labelWidth, 0, labelWidth, statusBarTop, Canvas.LineStyle.DASHED, Canvas.LineWidth.SMALL);
        while (index < 3){
            TextObject textObject = new TextObject();
            int x = (int) (labelWidth + ((chartWith- labelWidth) * scalar));
            textObject.setX(x);
            textObject.setY(statusBarMid);
            textObject.setText(String.format("%,d", (long) (highestValue * scalar)));

            textObject.draw(canvas);

            canvas.setColor("#C4C4C4");
            canvas.drawLine(x, 0, x, statusBarTop, Canvas.LineStyle.DASHED, Canvas.LineWidth.SMALL);

            index++;

            scalar += increment;
        }


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
}
