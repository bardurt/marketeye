package com.zygne.chart.chart.charts.tendency;

import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.model.chart.Camera;
import com.zygne.chart.chart.model.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.data.Quote;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TendencyChart implements Chart {

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;

    private Component component;
    private Camera camera;
    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private java.util.List<Object2d> objects = new ArrayList<>();
    private RendererImpl renderer;

    public TendencyChart(Component component) {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);
        this.component = component;

        renderer = new RendererImpl(camera);
    }

    @Override
    public void draw(Canvas g) {

    }

    @Override
    public void setBars(List<Quote> bars) {

    }

    @Override
    public void setWaterMark(String waterMark) {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setCurrentPrice(double price) {

    }
}
