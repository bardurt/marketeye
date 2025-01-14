package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.TextObject;

public class PriceScale extends Object2d {

    private static final int MAX_SIZE = 750;
    private double scale = 1;

    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public void draw(Canvas canvas) {

        TextObject label = new TextObject();
        label.setX(x);
        label.setAlignment(TextObject.Alignment.LEFT);
        label.setText("Vol :");
        label.setColor("#ffffff");

        for (int i = 0; i < MAX_SIZE; i++) {

            int level = i * 50;

            label.setY(-level);
            label.setText(String.format("%.5f", (level / scale)));
            label.draw(canvas);

            label.setY(-level - 25);
            label.setText(String.format("%.5f", ((level+25) / scale)));
            label.draw(canvas);
            label.setY(-level - 12);
            label.setText("-");
            label.draw(canvas);
            label.setY(-level - 37);
            label.setText("-");
            label.draw(canvas);

            canvas.setColor("#5B87A8");
            canvas.drawLine(x, -level, -width, -level, Canvas.LineStyle.DASHED, Canvas.LineWidth.SMALL);
        }

        for (int i = 0; i < MAX_SIZE/10; i++) {

            int level = i * 50;

            label.setY(level);
            label.setText(String.format("%.5f", (-level / scale)));
            label.draw(canvas);

            label.setY(level + 25);
            label.setText(String.format("%.5f", (-(level+25) / scale)));
            label.draw(canvas);
            label.setY(level + 12);
            label.setText("-");
            label.draw(canvas);
            label.setY(level + 37);
            label.setText("-");
            label.draw(canvas);

            canvas.setColor("#5B87A8");
            canvas.drawLine(x, level, -width, level, Canvas.LineStyle.DASHED, Canvas.LineWidth.SMALL);
        }
    }
}
