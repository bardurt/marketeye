package com.zygne.chart.chart.charts.pricechart;

import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.menu.ChartControls;
import com.zygne.chart.chart.menu.PriceScale;
import com.zygne.chart.chart.menu.Zoom;
import com.zygne.chart.chart.menu.indicators.CandleSticksIndicator;
import com.zygne.chart.chart.menu.indicators.SmoothedVolumeIndicator;
import com.zygne.chart.chart.menu.indicators.TimeIndicator;
import com.zygne.chart.chart.menu.indicators.VolumeIndicator;
import com.zygne.chart.chart.menu.indicators.VolumeProfileIndicator;
import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.chart.Camera;
import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.model.chart.Colors;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.TextObject;
import com.zygne.chart.chart.model.data.CandleSerie;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class PriceChart extends JPanel implements Chart,
        CandleSticksIndicator.Creator.Callback,
        VolumeProfileIndicator.Creator.Callback,
        VolumeIndicator.Creator.Callback,
        SmoothedVolumeIndicator.Creator.Callback,
        TimeIndicator.Creator.Callback,
        Zoom.Callback,
        ChartControls.Callback {
    private static final long THREAD_SLEEP_TIME = 30;

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
    private final List<Object2d> chartObjects = new ArrayList<>();
    private final RendererImpl renderer;
    private CandleSticksIndicator candleSticksIndicator = null;
    private VolumeProfileIndicator volumeProfileIndicator = null;
    private VolumeIndicator volumeIndicator = null;
    private TimeIndicator timeIndicator = null;
    private SmoothedVolumeIndicator smoothedVolumeIndicator = null;
    private final Zoom zoom;
    private final PriceScale priceScale = new PriceScale();
    private final ChartControls chartControls;

    public PriceChart() {
        setFocusable(true);
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);

        zoom = new Zoom(this);
        renderer = new RendererImpl(camera);
        chartControls = new ChartControls(this);

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

        priceScale.setX(canvasWidth - 75);
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
            CandleSerie candleSerie = ((CandleSerie) s);
            quotes.add(candleSerie);
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
        renderer.render(chartObjects);
    }

    private void refresh() {
        chartObjects.clear();
        chartObjects.add(waterMark);
        chartObjects.add(camera);


        if (smoothedVolumeIndicator != null) {
            chartObjects.add(smoothedVolumeIndicator);
        }

        if (volumeIndicator != null) {
            chartObjects.add(volumeIndicator);
        }

        if (timeIndicator != null) {
            chartObjects.add(timeIndicator);
        }

        if (candleSticksIndicator != null) {
            chartObjects.add(candleSticksIndicator);
        }

        chartObjects.add(priceScale);

        if (volumeProfileIndicator != null) {
            chartObjects.add(volumeProfileIndicator);
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
    }

    private void updateIndicators() {
        priceScale.setScale(scale);

        smoothedVolumeIndicator = null;
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

        new SmoothedVolumeIndicator.Creator().create(
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
    public void onVolumeIndicatorCreated(SmoothedVolumeIndicator volumeIndicator) {
        this.smoothedVolumeIndicator = volumeIndicator;
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
                camera.getWidth() - 225
        );
    }

    private void createCandleSticks() {
        candleSticksIndicator = null;

        new CandleSticksIndicator.Creator().create(
                this,
                bars,
                scale,
                barWidth
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
        if(candleSticksIndicator != null){
            candleSticksIndicator.adjust((x, y) -> {
                loadY = y;
                lastBar = x;
                centerCamera();
                updateIndicators();
            }, barWidth, scale);
        }
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
            c.visible = camera.inHorizontalSpace(c);
        }
    }

    private record ChartRunner(Component component) implements Runnable {

        @Override
        public void run() {

            boolean running = true;
            while (running) {
                EventQueue.invokeLater(component::repaint);

                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}