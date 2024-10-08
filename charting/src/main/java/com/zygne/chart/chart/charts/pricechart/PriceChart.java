package com.zygne.chart.chart.charts.pricechart;

import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.menu.*;
import com.zygne.chart.chart.menu.indicators.*;
import com.zygne.chart.chart.model.chart.*;
import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.data.CandleSerie;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriceChart extends JPanel implements Chart,
        CandleSticksIndicator.Creator.Callback,
        VolumeProfileIndicator.Creator.Callback,
        VolumeIndicator.Creator.Callback,
        TimeIndicator.Creator.Callback,
        Zoom.Callback,
        ChartControls.Callback {

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;
    private double scale = 1;
    private int barWidth = 1;
    private int lastBar = 0;

    private int loadY;

    private final List<CandleSerie> bars = new ArrayList<>();

    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private final Camera camera;
    private String waterMarkText = "";
    private TextObject waterMark;
    private final List<Object2d> objects = new ArrayList<>();
    private final RendererImpl renderer;
    private CandleSticksIndicator candleSticksIndicator = null;
    private VolumeProfileIndicator volumeProfileIndicator = null;
    private VolumeIndicator volumeIndicator = null;
    private TimeIndicator timeIndicator = null;
    private final Zoom zoom;
    private final PriceScale priceScale = new PriceScale();

    public PriceChart() {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);

        zoom = new Zoom(this);
        renderer = new RendererImpl(camera);
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

        reset();
        List<Serie> bars = series.get(0);

        if (!(bars.get(0) instanceof CandleSerie)) {
            throw new RuntimeException("Series items at [0] should be of type " + CandleSerie.class.getName());
        }

        List<CandleSerie> quotes = new ArrayList<>();

        for (Serie s : bars) {
            quotes.add((CandleSerie) s);
        }

        this.bars.clear();
        this.bars.addAll(quotes);
        this.bars.sort(new CandleSerie.TimeComparator());

        createCandleSticks();

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
        objects.add(camera);


        if (volumeIndicator != null) {
            objects.add(volumeIndicator);
        }

        if (timeIndicator != null) {
            objects.add(timeIndicator);
        }

        if (candleSticksIndicator != null) {
            objects.add(candleSticksIndicator);
        }

        objects.add(priceScale);

        if (volumeProfileIndicator != null) {
            objects.add(volumeProfileIndicator);
        }

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
    public void zoom(double level) {

    }

    @Override
    public void stretch(double level) {
        zoom.stretch(level);
    }

    private void updateIndicators() {
        priceScale.setScale(scale);

        volumeIndicator = null;
        volumeProfileIndicator = null;
        timeIndicator = null;

        if (candleSticksIndicator == null) {
            return;
        }

        createVolumeProfile();

        new VolumeIndicator.Creator().create(
                this,
                candleSticksIndicator.getCandleSticks(),
                150,
                canvasHeight - 20);

        new TimeIndicator.Creator().create(this,
                candleSticksIndicator.getCandleSticks(),
                canvasHeight,
                20);

    }


    @Override
    public void onCandleSticksIndicatorCreated(CandleSticksIndicator candleSticksIndicator, int x, int y) {
        this.candleSticksIndicator = candleSticksIndicator;
        this.lastBar = x;
        this.loadY = y;

        centerCamera();
        SwingUtilities.invokeLater(() -> {
            refresh();
            updateIndicators();
        });
    }

    @Override
    public void onVolumeIndicatorCreated(VolumeIndicator volumeIndicator) {
        this.volumeIndicator = volumeIndicator;
        refresh();
    }

    @Override
    public void onVolumeProfileCreated(VolumeProfileIndicator volumeProfileIndicator) {
        this.volumeProfileIndicator = volumeProfileIndicator;
        this.volumeProfileIndicator.setzOrder(1);
        refresh();
    }

    @Override
    public void onTimeIndicatorCreated(TimeIndicator timeIndicator) {
        this.timeIndicator = timeIndicator;
        refresh();
    }


    private void createVolumeProfile() {
        if (candleSticksIndicator == null) {
            return;
        }

        if (volumeProfileIndicator != null) {
            volumeProfileIndicator = null;
        }

        new VolumeProfileIndicator.Creator().create(
                this,
                candleSticksIndicator.getCandleSticks(),
                scale,
                150,
                camera.getWidth() - 200
        );
    }

    private void createCandleSticks() {
        candleSticksIndicator = null;

        int barSeparator = 0;
        new CandleSticksIndicator.Creator().create(
                this,
                bars,
                scale,
                barWidth,
                barSeparator
        );
    }

    private void reset() {
        bars.clear();
    }

    private void centerCamera() {
        camera.setViewPortY((-loadY - camera.getHeight() / 2) * -1);
        camera.setViewPortX((lastBar - camera.getWidth() / 2) * -1);
    }

    @Override
    public void onZoomChanged(Zoom.ZoomDetails zoomDetails) {
        scale = zoomDetails.zoomLevel();
        barWidth = (int) zoomDetails.stretchLevel();
        createCandleSticks();
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
        checkVisibility();
        createVolumeProfile();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(new AwtCanvas(g));
    }

    private void checkVisibility() {
        if (candleSticksIndicator == null) {
            return;
        }
        for (CandleStick c : candleSticksIndicator.getCandleSticks()) {
            c.visible = camera.inVerticalSpace(c);
        }
    }

    private record ChartRunner(Component component) implements Runnable {

        @Override
        public void run() {

            boolean running = true;
            while (running) {
                EventQueue.invokeLater(component::repaint);

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}