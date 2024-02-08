package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Line extends Object2d {
    private List<Point2d> point2dList = new ArrayList<>();
    private String color;

    private Canvas.LineWidth lineWidth = Canvas.LineWidth.MEDIUM;
    private Canvas.LineStyle lineStyle = Canvas.LineStyle.SOLID;

    public void addPoint(Point2d point) {
        point2dList.add(point);
    }

    public void setLineWidth(Canvas.LineWidth lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineStyle(Canvas.LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public Point2d getPoint(int i) {
        return point2dList.get(i);
    }

    public List<Point2d> getPoints() {
        return point2dList;
    }

    private void setColor() {
        if (colorSchema == null) {
            color = "";
            return;
        }
        switch (colorSchema) {
            case WHITE -> {
                color = "#EFF5F0";
            }
            case RED -> {
                color = "#D93333";
            }
            case ORANGE -> {
                color = "#D9B333";
            }
            case YELLOW -> {
                color = "#E4EB00";
            }
            case BLUE -> {
                color = "#00B2EB";
            }
            case GREEN -> {
                color = "#00EB35";
            }
            case HIGHLIGHT -> {
                color = "#B2E1BD";
            }
        }

    }

    @Override
    public void draw(Canvas canvas) {
        setColor();
        canvas.setColor(color);
        for (int i = 0; i < point2dList.size() - 1; i++) {
            Point2d p1 = point2dList.get(i);
            Point2d p2 = point2dList.get(i + 1);
            canvas.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), lineStyle, lineWidth);
        }
    }
}
