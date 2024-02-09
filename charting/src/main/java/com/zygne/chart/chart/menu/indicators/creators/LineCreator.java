package com.zygne.chart.chart.menu.indicators.creators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.menu.indicators.LineIndicator;
import com.zygne.chart.chart.model.chart.Line;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.Point2d;
import com.zygne.chart.chart.model.data.LineSerie;
import com.zygne.chart.chart.model.data.Serie;

import java.util.ArrayList;
import java.util.List;

public class LineCreator {
    public void create(Callback callback, List<List<Serie>> quoteList, double scalar, int barWidth) {

        Runnable r = () -> {
            int x;
            int maxSize = 0;
            LineIndicator lineIndicator = new LineIndicator();
            List<Line> lines = new ArrayList<>();
            for (List<Serie> quotes : quoteList) {
                if (quotes.size() > maxSize) {
                    maxSize = quotes.size();
                }
                x = 0;
                Line line = new Line();
                line.setName(quotes.get(0).getName());
                for (Serie e : quotes) {

                    LineSerie s = (LineSerie) e;

                    Point2d point2d = new Point2d(x, (int) (s.getY() * scalar) * -1);
                    point2d.setTimeStamp(e.getTimeStamp());
                    line.addPoint(point2d);

                    x += barWidth;
                }
                lines.add(line);
            }

            Line zeroLine = new Line();
            x = 0;
            for (int i = 0; i < maxSize; i++) {
                Point2d point2d = new Point2d(x, 0);

                zeroLine.addPoint(point2d);

                x += barWidth;

            }
            zeroLine.setLineWidth(Canvas.LineWidth.SMALL);
            zeroLine.setColorSchema(Object2d.ColorSchema.WHITE);
            lineIndicator.addLine(zeroLine);

            lines.get(0).setColorSchema(Object2d.ColorSchema.BLUE);
            lines.get(1).setColorSchema(Object2d.ColorSchema.GREEN);
            lines.get(2).setColorSchema(Object2d.ColorSchema.YELLOW);
            lines.get(3).setColorSchema(Object2d.ColorSchema.RED);

            for (Line l : lines) {
                lineIndicator.addLine(l);
            }

            callback.onLineIndicatorCreated(lineIndicator);
        };


        Thread t = new Thread(r);

        t.start();

    }

    public interface Callback {
        void onLineIndicatorCreated(LineIndicator lineIndicator);
    }
}