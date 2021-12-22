package com.zygne.zchart.chart;

import com.zygne.zchart.chart.model.chart.Object2d;

import java.util.List;

public interface Renderer {


    public void Render(List<Object2d> object2dList);
    public boolean sizeChanged(int width, int height);
}
