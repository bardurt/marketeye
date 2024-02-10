package com.zygne.chart.chart.charts.bar;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.menu.ChartControls;
import com.zygne.chart.chart.menu.PriceScale;
import com.zygne.chart.chart.menu.StatusBar;
import com.zygne.chart.chart.menu.Zoom;
import com.zygne.chart.chart.menu.indicators.BarIndicator;
import com.zygne.chart.chart.menu.indicators.TimeIndicator;
import com.zygne.chart.chart.menu.indicators.creators.BarIndicatorCreator;
import com.zygne.chart.chart.menu.indicators.creators.TimeCreator;
import com.zygne.chart.chart.model.chart.*;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BarChart extends JPanel implements Chart,
        BarIndicatorCreator.Callback,
        TimeCreator.Callback,
        Zoom.Callback,
        ChartControls.Callback {

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;
    private double scale = 20;
    private int barWidth = 12;

    private final List<Serie> barData = new ArrayList<>();

    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private final Camera camera;
    private String waterMarkText = "";
    private TextObject waterMark;
    private final List<Object2d> objects = new ArrayList<>();
    private final RendererImpl renderer;
    private final Zoom zoom;

    private BarIndicator barIndicator = null;
    private TimeIndicator timeIndicator = null;
    private final PriceScale priceScale = new PriceScale();

    public BarChart() {
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

        StatusBar statusBar = new StatusBar();
        statusBar.setWidth(canvasWidth);
        statusBar.setHeight(canvasHeight);
        int labelWidth = 60;
        statusBar.setLabelWidth(labelWidth);
        statusBar.setzOrder(2);

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
            if (!(items.get(0) instanceof BarSerie)) {
                throw new RuntimeException("Chart only works with " + BarSerie.class.getName());
            }
        }

        reset();
        this.barData.clear();
        this.barData.addAll(series.get(0));
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

        renderer.Render(objects);
        g.setColor("#ffffff");
    }

    private void refresh() {
        objects.clear();
        objects.add(waterMark);

        if (timeIndicator != null) {
            objects.add(timeIndicator);
        }

        if (barIndicator != null) {
            objects.add(barIndicator);
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

    private void updateIndicators() {
        priceScale.setScale(scale);
        timeIndicator = null;

        if (barIndicator == null) {
            return;
        }

        new TimeCreator().create(this,
                barIndicator.getBars(),
                canvasHeight,
                30,
                barWidth);
    }

    @Override
    public void onBarIndicatorCreated(BarIndicator barIndicator, int x, int y) {
        this.barIndicator = barIndicator;
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
        barIndicator = null;
        new BarIndicatorCreator().create(
                this,
                barData,
                scale,
                barWidth
        );
    }

    private void reset() {
        barData.clear();
    }

    private void centerCamera() {}

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
            System.out.println("Barchart Running");
            while (true) {
                EventQueue.invokeLater(component::repaint);
                try {
                    Thread.sleep(35);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}