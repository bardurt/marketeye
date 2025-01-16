package com.zygne.chart.chart;

import com.zygne.chart.chart.model.chart.Object2d;

import java.util.List;

public interface Renderer {
    void render(List<Object2d> object2dList);
    boolean sizeChanged(int width, int height);
}
