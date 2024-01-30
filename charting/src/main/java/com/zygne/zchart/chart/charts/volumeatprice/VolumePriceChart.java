package com.zygne.zchart.chart.charts.volumeatprice;

import com.zygne.zchart.chart.Chart;
import com.zygne.zchart.chart.RendererImpl;
import com.zygne.zchart.chart.menu.*;
import com.zygne.zchart.chart.model.chart.Camera;
import com.zygne.zchart.chart.model.chart.Canvas;
import com.zygne.zchart.chart.model.chart.Object2d;
import com.zygne.zchart.chart.model.chart.TextObject;
import com.zygne.zchart.chart.model.chart.VolumeProfileBar;
import com.zygne.zchart.chart.model.data.Quote;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolumePriceChart extends MouseInputAdapter implements Chart,
        PercentileBar.Listener {

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;
    private final int labelWidth = 60;
    private final int paddingRight = 30;

    private Component component;

    private java.util.List<VolumeProfileBar> volumeProfileBars = new ArrayList<>();
    private java.util.List<Quote> bars = new ArrayList<>();
    private java.util.List<Quote> quotes = new ArrayList<>();
    private long highestValue = 0;
    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private Camera camera;
    private int startY;
    private int barHeight = 10;
    private String waterMarkText = "";
    private String titleText = "";
    private TextObject waterMark;
    private StatusBar statusBar;
    private PercentileBar percentileBar;
    private TopBar topBar;
    private java.util.List<Object2d> objects = new ArrayList<>();
    private RendererImpl renderer;
    private int padding = 50;
    private double percentile = 90;

    private double chartTop = 0;
    private double chartBottom = 0;

    public VolumePriceChart(Component component) {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);
        this.component = component;


        renderer = new RendererImpl(camera);
    }

    private void setUp() {
        createButtons();
        waterMark = new TextObject(0, 0, canvasWidth, canvasHeight);
        waterMark.setFontSize(TextObject.FontSize.LARGE);
        waterMark.setText(waterMarkText);
        waterMark.setzOrder(-1);

        statusBar = new StatusBar();
        statusBar.setWidth(canvasWidth);
        statusBar.setHeight(canvasHeight);
        statusBar.setLabelWidth(labelWidth);
        statusBar.setzOrder(2);

        topBar = new TopBar();
        topBar.setX(0);
        topBar.setY(0);
        topBar.setWidth(canvasWidth);
        topBar.setHeight(canvasHeight);
        topBar.setzOrder(2);
        topBar.init();
        topBar.setLabelText(titleText + " " + percentile + "%");

        if (bars.isEmpty()) {
            return;
        }

        Collections.sort(bars, new Quote.VolumeComparator());
        Collections.reverse(bars);
        highestValue = bars.get(0).getVolume();

        statusBar.setHighestValue(highestValue);

        volumeProfileBars.clear();
        Collections.sort(bars, new Quote.PriceComparator(Quote.PriceComparator.SORT_ORDER_HIGH));
        Collections.reverse(bars);

        double maxValue = (bars.get(0).getHigh() * 1.05);

        maxValue = Math.ceil(maxValue);

        if (Double.toString(maxValue).length() > 6) {
            padding = 60;
        } else {
            padding = 40;
        }

        statusBar.setLabelWidth(padding);

        int y = barHeight;

        for (Quote e : bars) {

            if (e.getPercentile() >= percentile || e.isCurrentPrice()) {
                VolumeProfileBar b = new VolumeProfileBar();
                b.setX(1);
                b.setY(y);
                b.setPadding(padding);
                b.setHeight(barHeight);

                double percent = (e.getVolume() / (double) highestValue);

                b.setWidth(camera.getWidth());
                b.setPercentage(percent);

                //ChartUtilities.setColorSchemeTop2(b, percent);

                b.setText(String.format("%.2f", e.getHigh()));
                b.setVolume(e.getVolume());
                b.setzOrder(0);
                volumeProfileBars.add(b);
                y += barHeight;
                b.setPrintLabel(true);

                if (e.isCurrentPrice()) {
                    b.setCanvasWidth(canvasWidth);
                    b.setCurrentPrice(true);
                }

                volumeProfileBars.add(b);
            }

            chartTop = 0 - canvasHeight/2;
            chartBottom =  y + canvasHeight/2;

        }
    }

    private void createButtons() {
        percentileBar = new PercentileBar();
        percentileBar.setListener(this);
        percentileBar.setX(canvasWidth - 120);
        percentileBar.setY(2);
        percentileBar.setWidth(100);
        percentileBar.setHeight(120);
        percentileBar.init();
        percentileBar.setzOrder(2);
    }

    @Override
    public void setBars(List<Quote> bars) {
        this.quotes.clear();
        this.quotes.addAll(bars);
        this.bars.clear();
        this.bars.addAll(bars);
        this.camera.setViewPortY(0);
        this.camera.setViewPortX(0);
        setUp();
        refresh();
        component.repaint();
    }

    @Override
    public void setCurrentPrice(double price) {
        Quote q = new Quote();
        q.setHigh(price);
        q.setCurrentPrice(true);
        bars.clear();
        bars.addAll(quotes);
        bars.add(q);
        this.camera.setViewPortY(0);
        this.camera.setViewPortX(0);
        setUp();
        refresh();
        component.repaint();
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
            refresh();
        }

        renderer.Render(objects);
    }

    private void refresh() {
        objects.clear();
        objects.add(statusBar);
        objects.add(waterMark);
        objects.add(topBar);
        objects.addAll(volumeProfileBars);
        if (percentileBar != null) {
            objects.add(percentileBar);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        int x = e.getX();
        int y = e.getY();

        int mouseX = (int) camera.getMouseX(x);
        int mouseY = (int) camera.getMouseY(y);
        for (VolumeProfileBar b : volumeProfileBars) {
            if (b.inside(mouseX, mouseY)) {
                b.highLight(true);
                topBar.setStatusText("Price : " + b.getText() + ", Vol " + b.getVolumeTest());
                break;
            }
        }

        component.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        topBar.setStatusText("");

        for (VolumeProfileBar b : volumeProfileBars) {
            b.highLight(false);
        }

        if (percentileBar != null) {
            percentileBar.inside(e.getX(), e.getY());
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
        int dy = startY - e.getY();
        int y = (int) (camera.getViewPortY() - (dy / 100));

        camera.setViewPortY(y);

        component.repaint();
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

    @Override
    public void increasePercentile() {
        this.percentile++;

        if (this.percentile > 99) {
            this.percentile = 99;
            return;
        }

        volumeProfileBars.clear();
        setUp();
        createButtons();
        camera.setViewPortY(0);
        refresh();
        component.repaint();
    }

    @Override
    public void decreasePercentile() {
        this.percentile--;

        if (this.percentile < 90) {
            this.percentile = 90;
            return;
        }

        volumeProfileBars.clear();
        setUp();
        createButtons();
        camera.setViewPortY(0);
        refresh();
        component.repaint();
    }
}