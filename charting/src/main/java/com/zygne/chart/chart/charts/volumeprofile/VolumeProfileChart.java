package com.zygne.chart.chart.charts.volumeprofile;

import com.zygne.chart.chart.Chart;
import com.zygne.chart.chart.RendererImpl;
import com.zygne.chart.chart.menu.*;
import com.zygne.chart.chart.model.chart.*;
import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.data.PriceBox;
import com.zygne.chart.chart.model.data.Quote;
import com.zygne.chart.chart.model.data.VolumeProfileGroup;
import com.zygne.chart.chart.util.GroupingGenerator;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolumeProfileChart extends MouseInputAdapter implements Chart,
        GroupingBar.Listener {

    private static final int BAR_HEIGHT_MIN = 3;
    private static final int BAR_HEIGHT_MAX = 30;

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 720;
    private final int labelWidth = 80;
    private final int paddingRight = 30;
    private final int maxBars = 400;

    private Component component;

    private List<VolumeProfileBar> volumeProfileBars = new ArrayList<>();
    private List<Quote> quotes = new ArrayList<>();
    private List<VolumeProfileGroup> volumeProfileGroups = new ArrayList<>();
    private long highestValue = 0;
    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private double grouping = -1;
    private Camera camera;
    private int startY;
    private int startX;
    private int barHeight = 5;
    private String waterMarkText = "";
    private String titleText = "";
    private TextObject waterMark;
    private GroupingBar groupingBar;
    private StatusBar statusBar;
    private TopBar topBar;
    private List<Object2d> objects = new ArrayList<>();
    private RendererImpl renderer;
    private int padding = 50;
    private Double currentPrice = null;

    public VolumeProfileChart(Component component) {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth + 20);
        this.component = component;


        renderer = new RendererImpl(camera);
    }

    private void setUp() {
        createButtons();

        waterMark = new TextObject(0, 0, canvasWidth, canvasHeight);
        waterMark.setFontSize(TextObject.FontSize.LARGE);
        waterMark.setText(waterMarkText);
        waterMark.setzOrder(-1);
        waterMark.setColor("#003D7A");

        statusBar = new StatusBar();
        statusBar.setWidth(canvasWidth);
        statusBar.setHeight(canvasHeight);
        statusBar.setLabelWidth(60);
        statusBar.setzOrder(2);

        topBar = new TopBar();
        topBar.setX(0);
        topBar.setY(0);
        topBar.setWidth(canvasWidth);
        topBar.setHeight(canvasHeight);
        topBar.setzOrder(2);
        topBar.init();
        topBar.setLabelText(titleText + " G= " + grouping);

        if (quotes.isEmpty()) {
            return;
        }

        double maxValue = 0;
        double minValue = 0;

        Collections.sort(quotes, new Quote.PriceComparator(Quote.PriceComparator.SORT_ORDER_HIGH));
        Collections.reverse(quotes);
        maxValue = (quotes.get(0).getHigh() * 1.05);

        maxValue = Math.ceil(maxValue);

        if (Double.toString(maxValue).length() > 6) {
            padding = 60;
        } else {
            padding = 40;
        }

        statusBar.setLabelWidth(padding);

        double difference = maxValue - minValue;

        double increment = difference / (double) maxBars;

        increment = GroupingGenerator.generateGrouping(increment);

        if (grouping == -1) {
            grouping = increment;
        }

        double start = Math.floor(minValue);

        while (start < maxValue) {

            VolumeProfileGroup b = new VolumeProfileGroup();
            PriceBox priceBox = new PriceBox();
            priceBox.setStart(start);
            priceBox.setEnd(start + grouping - 0.01);
            b.setPriceBox(priceBox);
            volumeProfileGroups.add(b);
            start += grouping;
        }

        for (Quote e : quotes) {
            for (VolumeProfileGroup g : volumeProfileGroups) {
                if (g.getPriceBox().inside(e.getHigh())) {
                    g.setVolume(g.getVolume() + e.getVolume());
                }
            }
        }

        Collections.sort(volumeProfileGroups, new VolumeProfileGroup.VolumeComparator());
        Collections.reverse(volumeProfileGroups);
        highestValue = volumeProfileGroups.get(0).getVolume();

        statusBar.setHighestValue(highestValue);

        refresh();
    }

    private void createBars() {
        volumeProfileBars.clear();
        Collections.sort(volumeProfileGroups, new VolumeProfileGroup.PriceComparator());
        Collections.reverse(volumeProfileGroups);

        int y = barHeight;

        for (int i = 0; i < volumeProfileGroups.size(); i++) {
            VolumeProfileGroup vp = volumeProfileGroups.get(i);

            VolumeProfileBar b = new VolumeProfileBar();
            b.setX(1);
            b.setY(y);
            b.setPadding(padding);
            b.setHeight(barHeight);
            b.setCanvasWidth(canvasWidth);

            double percent = (vp.getVolume() / (double) highestValue);
            b.setWidth(camera.getWidth());
            b.setPercentage(percent);

            //ChartUtilities.setColorScheme(b, percent);

            b.setText(String.format("%.2f", vp.getPrice()));
            b.setVolume(vp.getVolume());
            b.setzOrder(0);

            y += barHeight;

            if (barHeight > 10) {
                b.setPrintLabel(true);
            } else if (barHeight > 8) {
                if (i % 2 == 0) {
                    b.setPrintLabel(true);
                }
            } else if (barHeight > 5) {
                if (i % 3 == 0) {
                    b.setPrintLabel(true);
                }
            } else {
                if (i % 5 == 0) {
                    b.setPrintLabel(true);
                }
            }


            if (currentPrice != null) {
                if (vp.getPriceBox().inside(currentPrice)) {
                    b.setCurrentPrice(true);
                }
            }

            volumeProfileBars.add(b);

        }
    }

    private void createButtons() {
        groupingBar = new GroupingBar();
        groupingBar.setListener(this);
        groupingBar.setX(canvasWidth - 120);
        groupingBar.setY(2);
        groupingBar.setWidth(100);
        groupingBar.setHeight(120);
        groupingBar.init();
        groupingBar.setzOrder(2);
    }

    @Override
    public void setBars(List<Quote> bars) {
        this.currentPrice = null;
        this.grouping = -1;
        this.quotes.clear();
        this.quotes.addAll(bars);
        this.volumeProfileGroups.clear();
        this.camera.setX(0);
        this.camera.setY(0);
        setUp();
        createBars();
        refresh();
    }

    @Override
    public void setCurrentPrice(double price) {
        this.currentPrice = price;
        this.volumeProfileGroups.clear();
        this.camera.setX(0);
        this.camera.setY(0);
        setUp();
        createBars();
        refresh();
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
            createBars();
            refresh();
        }

        renderer.Render(objects);
    }

    private void refresh() {
        objects.clear();
        objects.add(statusBar);
        objects.add(waterMark);
        objects.add(topBar);
        objects.add(groupingBar);
        objects.addAll(volumeProfileBars);

    }

    public void setGrouping(double value) {
        volumeProfileGroups.clear();
        grouping = value;
        setUp();
        createBars();

        refresh();
        component.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        startX = e.getX();
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

        refresh();
        component.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        topBar.setStatusText("");

        for (VolumeProfileBar b : volumeProfileBars) {
            b.highLight(false);
        }

        int x = e.getX();
        int y = e.getY();

        groupingBar.inside(x, y);

        int dx = startX - e.getX();

        if (Math.abs(dx) > camera.getWidth() / 3) {
            if (dx < 0) {
                zoomIn();
            } else {
                zoomOut();
            }

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
        refresh();
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
        titleText = title;
        topBar.setLabelText(title);
    }

    @Override
    public void onIncreaseSelected() {
        grouping = GroupingGenerator.increment(grouping);
        //camera.setViewPortY(0);
        setGrouping(grouping);
    }

    @Override
    public void onDecreaseSelected() {
        grouping = GroupingGenerator.decrement(grouping);
        //camera.setViewPortY(0);
        setGrouping(grouping);
    }

    public void zoomIn() {
        this.barHeight++;

        if (this.barHeight > BAR_HEIGHT_MAX) {
            this.barHeight = BAR_HEIGHT_MAX;
            return;
        }

        volumeProfileGroups.clear();
        setUp();
        createButtons();
        createBars();
        //camera.setViewPortY(0);
        refresh();
        component.repaint();
    }

    public void zoomOut() {
        this.barHeight--;

        if (this.barHeight < BAR_HEIGHT_MIN) {
            this.barHeight = BAR_HEIGHT_MIN;
            return;
        }

        volumeProfileGroups.clear();
        setUp();
        createButtons();
        createBars();
        //camera.setViewPortY(0);
        refresh();
        component.repaint();
    }

}
