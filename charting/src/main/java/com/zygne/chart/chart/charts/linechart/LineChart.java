package com.zygne.chart.chart.charts.linechart;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.menu.*;
import com.zygne.chart.chart.menu.indicators.*;
import com.zygne.chart.chart.model.chart.*;
import com.zygne.chart.chart.model.data.LineSerie;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineChart extends JPanel implements Chart,
        LineIndicator.Creator.Callback,
        TimeIndicator.Creator.Callback,
        Zoom.Callback,
        ChartControls.Callback{

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;
    private double scale = 20;
    private int barWidth = 12;

    private final List<List<Serie>> lineData = new ArrayList<>();

    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private final Camera camera;
    private String waterMarkText = "";
    private TextObject waterMark;
    private final List<Object2d> objects = new ArrayList<>();
    private final RendererImpl renderer;
    private final Zoom zoom;

    private LineIndicator lineIndicator = null;
    private TimeIndicator timeIndicator = null;
    private final List<TextObject> seriesNames = new ArrayList<>();
    private final PriceScale priceScale = new PriceScale();

    public LineChart() {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);
        renderer = new RendererImpl(camera);
        zoom = new Zoom(this);
        ChartControls chartControls = new ChartControls(this);

        addMouseMotionListener(chartControls);
        addMouseListener(chartControls);
        setUp();

        ChartRunner chartRunner = new ChartRunner(this);
        Thread t = new Thread(chartRunner);
        t.start();
    }

    private void setUp() {
        waterMark = new TextObject(0, 0, canvasWidth, canvasHeight);
        waterMark.setFontSize(TextObject.FontSize.LARGE);
        waterMark.setText(waterMarkText);
        waterMark.setzOrder(-1);
        this.waterMark.setColor("#00306C");

        priceScale.setX(canvasWidth - 50);
        priceScale.setY(1);
        priceScale.setWidth(100);
        priceScale.setHeight(camera.getHeight());
        priceScale.setzOrder(1);
        priceScale.setScale(scale);
    }

    @Override
    public void setSeries(List<List<Serie>> series) {
        if (series.isEmpty()) {
            return;
        }

        for (List<Serie> items : series) {
            if (!(items.get(0) instanceof LineSerie)) {
                throw new RuntimeException("Chart only works with " + LineSerie.class.getName());
            }
        }

        reset();
        this.lineData.clear();
        this.lineData.addAll(series);
        createChartData();

        zoom.reset();
    }

    @Override
    public void draw(Canvas g) {
        renderer.setCanvas(g);
        if (renderer.sizeChanged(g.getWidth(), g.getHeight())) {
            canvasHeight = g.getHeight();
            canvasWidth = g.getWidth();
            camera.setHeight(g.getHeight());
            camera.setWidth(g.getWidth());

            setUp();
            updateIndicators();
        }

        g.setColor(Colors.BLUE_DARK);
        renderer.Render(objects);
    }

    private void refresh() {
        objects.clear();
        objects.add(waterMark);

        if (timeIndicator != null) {
            objects.add(timeIndicator);
        }

        if (lineIndicator != null) {
            objects.add(lineIndicator);
        }

        if (!seriesNames.isEmpty()) {
            objects.addAll(seriesNames);
        }

        objects.add(priceScale);

    }

    @Override
    public void setWaterMark(String waterMark) {
        this.waterMarkText = waterMark;
        this.waterMark.setText(waterMarkText);
        this.waterMark.setColor("#003D7A");

    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public void stretch(double level) {
        zoom.stretch(level);
    }

    private void updateIndicators() {
        priceScale.setScale(scale);
        timeIndicator = null;

        if (lineIndicator == null) {
            return;
        }

        new TimeIndicator.Creator().create(this,
                lineIndicator.getLines().get(2),
                canvasHeight - 10,
                20,
                barWidth);
    }

    @Override
    public void onLineIndicatorCreated(LineIndicator lineIndicator) {
        this.lineIndicator = lineIndicator;
        int textX = 5;
        for (Line l : lineIndicator.getLines()) {
            TextObject t1 = new TextObject(textX, 10, 20, 20);
            t1.setText(l.getName());
            t1.setColorSchema(l.getColorSchema());
            t1.setFontSize(TextObject.FontSize.MEDIUM);
            t1.setzOrder(-1);

            seriesNames.add(t1);
            textX += 120;
        }
        SwingUtilities.invokeLater(() -> {
            refresh();
            centerCamera();
            updateIndicators();
        });
    }

    @Override
    public void onTimeIndicatorCreated(TimeIndicator timeIndicator) {
        this.timeIndicator = timeIndicator;
        SwingUtilities.invokeLater(this::refresh);
    }


    private void createChartData() {
        lineIndicator = null;
        new LineIndicator.Creator().create(
                this,
                lineData,
                scale,
                barWidth
        );
    }

    private void reset() {
        lineData.clear();
    }

    private void centerCamera() {
        camera.setViewPortY(canvasHeight / 2);
        camera.setViewPortX(0);
    }


    @Override
    public void onZoomChanged(Zoom.ZoomDetails zoomDetails) {
        scale = zoomDetails.zoomLevel();
        barWidth = (int) zoomDetails.stretchLevel();
        createChartData();
    }

    @Override
    public void onDragLeft(int dist) {
        zoom.stretch();
    }

    @Override
    public void onDragRight(int dist) {
        zoom.shrink();
    }

    @Override
    public void onDragUp(int dist) {
        zoom.zoomIn();
    }

    @Override
    public void onDragDown(int dist) {
        zoom.zoomOut();
    }

    @Override
    public void onDrag(int dx, int dy) {
        int y = camera.getViewPortY() - (dy / 100);

        int x = camera.getViewPortX() - (dx / 100);

        camera.setViewPortY(y);
        camera.setViewPortX(x);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(new AwtCanvas(g));
    }

    private record ChartRunner(Component component) implements Runnable {

        @Override
        public void run() {
            while (true) {
                EventQueue.invokeLater(component::repaint);
                try {
                    Thread.sleep(35);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public void zoom(double level) {

    }
}