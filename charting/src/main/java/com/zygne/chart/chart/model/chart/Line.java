package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Line extends Object2d {

    private List<Point2d> point2dList = new ArrayList<>();
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.setColor(color);
        for (int i = 0; i < point2dList.size() - 1; i++) {
            Point2d p1 = point2dList.get(i);
            Point2d p2 = point2dList.get(i + 1);
            canvas.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), lineStyle, lineWidth);
        }
    }
}
