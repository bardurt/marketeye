package com.zygne.chart.chart.charts.linechart;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.menu.*;
import com.zygne.chart.chart.menu.indicators.*;
import com.zygne.chart.chart.menu.indicators.creators.*;
import com.zygne.chart.chart.model.chart.*;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LineChart extends MouseInputAdapter implements Chart,
        OptionsMenu.Listener,
        LineCreator.Callback,
        TimeCreator.Callback,
        Zoom.Callback {

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;
    private final int labelWidth = 60;
    private double scale = 20;
    private int barWidth = 12;
    private int lastBar = 0;
    private boolean scaling = false;
    private boolean stretching = false;

    private int loadY;

    private final List<List<Serie>> bars = new ArrayList<>();

    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private final Camera camera;
    private int startY;
    private int startX;
    private String waterMarkText = "";
    private String titleText = "";
    private TextObject waterMark;
    private TextObject copyright;
    private TopBar topBar;
    private final List<Object2d> objects = new ArrayList<>();
    private final RendererImpl renderer;
    private final Zoom zoom;
    private final int percentile = 90;

    private LineIndicator lineIndicator = null;
    private TimeIndicator timeIndicator = null;
    private List<TextObject> seriesNames = new ArrayList<>();

    private final PriceScale priceScale = new PriceScale();

    public LineChart() {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);
        this.priceScale.setNegative(true);
        renderer = new RendererImpl(camera);
        zoom = new Zoom(this);

        setUp();
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
        reset();
        this.bars.clear();
        this.bars.addAll(series);
        createCandleSticks();
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
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        startX = e.getX();

        if (startX > camera.getWidth() - 50) {
            scaling = true;
        } else if (startY > camera.getHeight() - 50) {
            stretching = true;
        } else {
            int x = e.getX();
            int y = e.getY();

            int mouseX = (int) camera.getMouseX(x);
            int mouseY = (int) camera.getMouseY(y);
            System.out.println("mouse x " + mouseX + " mouse y " + mouseY);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int dy = startY - e.getY();
        int dx = startX - e.getX();

        if (scaling) {
            if (dy > 2) {
                scaleUp(1);
            }
            if (dy < -2) {
                scaleDown(1);
            }

            scaling = false;
            return;
        }

        if (stretching) {
            if (dx > 2) {
                scaleUp(0);
            }
            if (dx < -2) {
                scaleDown(0);
            }

            stretching = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (scaling || stretching) {
            return;
        }

        int dy = startY - e.getY();
        int dx = startX - e.getX();

        int y = camera.getViewPortY() - (dy / 100);

        int x = camera.getViewPortX() - (dx / 100);

        camera.setViewPortY(y);
        camera.setViewPortX(x);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
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


    public void scaleUp(int what) {
        switch (what) {
            case 0 -> {
                zoom.stretch();
            }
            case 1 -> {
                zoom.zoomIn();
            }
        }
    }

    public void scaleDown(int what) {
        switch (what) {
            case 0 -> {
                zoom.shrink();
            }
            case 1 -> {
                zoom.zoomOut();
            }
        }

    }


    @Override
    public void onOptionsSelected(OptionsMenu.OptionItem options) {
        if (options == OptionsMenu.OptionItem.CENTER_CHART) {
            centerCamera();
        }
    }

    private void updateIndicators() {
        priceScale.setScale(scale);
        timeIndicator = null;

        if (lineIndicator == null) {
            return;
        }

        new TimeCreator().create(this,
                lineIndicator.getLines().get(2),
                canvasHeight - 10,
                20,
                barWidth);
    }

    private void adjustCamera() {
        camera.setViewPortY((-loadY - camera.getHeight()) * -1);
    }

    @Override
    public void onLineIndicatorCreated(LineIndicator lineIndicator, int x, int y) {
        this.lineIndicator = lineIndicator;
        this.lastBar = x;
        this.loadY = y;

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
        SwingUtilities.invokeLater(() -> {
            refresh();
        });
    }


    private void createCandleSticks() {
        lineIndicator = null;
        new LineCreator().create(
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
        camera.setViewPortY(canvasHeight / 2);
        camera.setViewPortX(0);
    }

    @Override
    public void onZoomLevelSet(double scalar) {
        scale = scalar;
        createCandleSticks();
    }

    @Override
    public void onStretchSet(double scalar) {
        barWidth = (int) scalar;
        createCandleSticks();
    }
}