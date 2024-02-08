package com.zygne.chart.chart.menu.indicators.creators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.menu.indicators.LineIndicator;
import com.zygne.chart.chart.model.chart.Line;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.Point2d;
import com.zygne.chart.chart.model.data.Quote;

import java.util.ArrayList;
import java.util.List;

public class LineCreator {
    public void create(Callback callback, List<List<Quote>> quoteList, double scalar, int barWidth) {

        Runnable r = () -> {
            int x = 0;
            int maxSize = 0;
            LineIndicator lineIndicator = new LineIndicator();
            List<Line> lines = new ArrayList<>();
            for (List<Quote> quotes : quoteList) {
                if (quotes.size() > maxSize) {
                    maxSize = quotes.size();
                }
                x = 0;
                Line line = new Line();
                for (Quote e : quotes) {

                    Point2d point2d = new Point2d(x, (int) (e.getClose() * scalar) * -1);
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

            int lastBar = x;

            callback.onLineIndicatorCreated(lineIndicator, lastBar, 0);
        };


        Thread t = new Thread(r);

        t.start();

    }

    public interface Callback {
        void onLineIndicatorCreated(LineIndicator lineIndicator, int x, int y);
    }
}