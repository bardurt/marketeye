package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Line;
import com.zygne.chart.chart.model.chart.Object2d;

import java.util.ArrayList;
import java.util.List;


public class LineIndicator extends Object2d {

    private static final int MAX_SIZE = 5;
    private final List<Line> lines = new ArrayList<>();

    public void addLine(Line line) {

        if (this.lines.size() > MAX_SIZE) {
            return;
        }

        this.lines.add(line);
    }

    public List<Line> getLines() {
        return lines;
    }

    @Override
    public void draw(Canvas canvas) {
        for (Line line : lines) {
            line.draw(canvas);
        }
    }
}