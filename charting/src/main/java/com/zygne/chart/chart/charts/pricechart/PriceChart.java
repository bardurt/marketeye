package com.zygne.chart.chart.charts.pricechart;

import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.charts.linechart.LineChart;
import com.zygne.chart.chart.menu.*;
import com.zygne.chart.chart.menu.indicators.*;
import com.zygne.chart.chart.menu.indicators.creators.*;
import com.zygne.chart.chart.model.chart.*;
import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.chart.chart.model.data.VolumeSerie;
import com.zygne.chart.chart.util.ZoomHelper;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PriceChart extends JPanel implements Chart,
        OptionsMenu.Listener,
        CandleSticksCreator.Callback,
        VolumeProfileCreator.Callback,
        VolumeIndicatorCreator.Callback,
        TimeCreator.Callback,
        Zoom.Callback,
        ChartControls.Callback {

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;
    private double scale = 1;
    private int barWidth = 1;
    private int lastBar = 0;

    private int loadY;

    private final List<BarSerie> bars = new ArrayList<>();
    private final List<VolumeSerie> volumeProfile = new ArrayList<>();

    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private final Camera camera;
    private String waterMarkText = "";
    private String titleText = "";
    private TextObject waterMark;
    private TextObject copyright;
    private TopBar topBar;
    private final List<Object2d> objects = new ArrayList<>();
    private final RendererImpl renderer;
    private final int percentile = 90;

    private CandleSticksIndicator candleSticksIndicator = null;
    private VolumeProfileIndicator volumeProfileIndicator = null;
    private VolumeIndicator volumeIndicator = null;
    private TimeIndicator timeIndicator = null;
    private final Zoom zoom;

    private boolean firstLoad = false;

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

        copyright = new TextObject(0, 0, canvasWidth, 50);
        copyright.setFontSize(TextObject.FontSize.MEDIUM);
        copyright.setText("Zygne Chart");
        copyright.setzOrder(-1);
        this.copyright.setColor("#00306C");

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

        topBar = new TopBar();
        topBar.setX(0);
        topBar.setY(0);
        topBar.setWidth(canvasWidth);
        topBar.setHeight(canvasHeight);
        topBar.setzOrder(2);
        topBar.init();
        topBar.setLabelText(titleText + " " + percentile + "%");
    }


    @Override
    public void setSeries(List<List<Serie>> series) {
        if (series.isEmpty()) {
            return;
        }

        firstLoad = true;

        reset();
        List<Serie> bars = series.get(0);

        if (!(bars.get(0) instanceof BarSerie)) {
            throw new RuntimeException("Series items at [0] should be of type " + BarSerie.class.getName());
        }

        List<BarSerie> quotes = new ArrayList<>();

        for (Serie s : bars) {
            quotes.add((BarSerie) s);
        }


        this.bars.clear();
        this.bars.addAll(quotes);
        this.bars.sort(new BarSerie.TimeComparator());

        if (series.size() == 2) {
            List<Serie> volumeProfileItems = series.get(1);
            if (!(volumeProfileItems.get(0) instanceof VolumeSerie)) {
                throw new RuntimeException("Series items at [0] should be of type " + VolumeSerie.class.getName());
            }
            for (Serie s : volumeProfileItems) {
                volumeProfile.add((VolumeSerie) s);
            }
        }


        createCandleSticks();

        if (!volumeProfile.isEmpty()) {
            createVolumeProfile();
        }

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
        objects.add(copyright);


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
        this.titleText = title;
        topBar.setLabelText(title + " " + percentile + "%");
    }


    @Override
    public void onOptionsSelected(OptionsMenu.OptionItem options) {
        if (options == OptionsMenu.OptionItem.CENTER_CHART) {
            centerCamera();
        }
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

        new VolumeIndicatorCreator().create(
                this,
                candleSticksIndicator.getCandleSticks(),
                150,
                canvasHeight - 20);

        new TimeCreator().create(this,
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
        if (volumeProfileIndicator != null) {
            volumeProfileIndicator = null;
        }

        new VolumeProfileCreator().create(this,
                this.volumeProfile,
                scale,
                150,
                camera.getWidth() - 200
        );
    }

    private void createCandleSticks() {
        candleSticksIndicator = null;

        int barSeparator = 0;
        new CandleSticksCreator().create(
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
        System.out.println("x " + lastBar + " y" + loadY);
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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(new AwtCanvas(g));
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