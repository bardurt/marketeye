package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Line;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.Point2d;
import com.zygne.chart.chart.model.chart.TextObject;

import java.util.ArrayList;
import java.util.List;

public class PriceScale extends Object2d {

    private static final int MAX_SIZE = 750;

    private List<Object2d> items = new ArrayList<>();

    public void setScale(double scale) {
        this.items.clear();

        for (int i = 0; i < MAX_SIZE; i++) {

            int level = i * 50;

            TextObject label1 = new TextObject();
            label1.setX(x);
            label1.setAlignment(TextObject.Alignment.LEFT);
            label1.setY(-level);
            label1.setText(String.format("%.5f", (level / scale)));
            items.add(label1);

            TextObject label2 = new TextObject();
            label2.setX(x);
            label2.setAlignment(TextObject.Alignment.LEFT);
            label2.setY(-level - 25);
            label2.setText(String.format("%.5f", ((level+25) / scale)));
            items.add(label2);

            TextObject label3 = new TextObject();
            label3.setX(x);
            label3.setAlignment(TextObject.Alignment.LEFT);
            label3.setY(-level - 12);
            label3.setText("-");
            items.add(label3);

            TextObject label4 = new TextObject();
            label4.setX(x);
            label4.setAlignment(TextObject.Alignment.LEFT);
            label4.setY(-level - 37);
            label4.setText("-");
            items.add(label4);

            Line line = new Line();
            line.addPoint(new Point2d(x, -level));
            line.addPoint(new Point2d(-width, -level));
            line.setLineStyle(Canvas.LineStyle.DASHED);
            line.setLineWidth(Canvas.LineWidth.SMALL);
            line.setColorSchema(ColorSchema.BLUE_METAL);
            items.add(line);
        }

        for (int i = 0; i < MAX_SIZE/10; i++) {

            int level = i * 50;

            TextObject label1 = new TextObject();
            label1.setX(x);
            label1.setAlignment(TextObject.Alignment.LEFT);
            label1.setY(level);
            label1.setText(String.format("%.5f", (-level / scale)));
            items.add(label1);

            TextObject label2 = new TextObject();
            label2.setX(x);
            label2.setAlignment(TextObject.Alignment.LEFT);
            label2.setY(level + 25);
            label2.setText(String.format("%.5f", (-(level+25) / scale)));
            items.add(label2);

            TextObject label3 = new TextObject();
            label3.setX(x);
            label3.setAlignment(TextObject.Alignment.LEFT);
            label3.setY(level + 12);
            label3.setText("-");
            items.add(label3);

            TextObject label4 = new TextObject();
            label4.setX(x);
            label4.setAlignment(TextObject.Alignment.LEFT);
            label4.setY(level + 37);
            label4.setText("-");
            items.add(label4);

            Line line = new Line();
            line.addPoint(new Point2d(x, level));
            line.addPoint(new Point2d(-width, level));
            line.setLineStyle(Canvas.LineStyle.DASHED);
            line.setLineWidth(Canvas.LineWidth.SMALL);
            line.setColorSchema(ColorSchema.BLUE_METAL);
            items.add(line);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        for (Object2d item : items) {
            item.draw(canvas);
        }
    }
}
