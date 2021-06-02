package com.zygne.zchart.volumeprofile;

import com.zygne.zchart.volumeprofile.menu.BarSizeMenu;
import com.zygne.zchart.volumeprofile.menu.GroupingMenu;
import com.zygne.zchart.volumeprofile.menu.StatusBar;
import com.zygne.zchart.volumeprofile.menu.TopBar;
import com.zygne.zchart.volumeprofile.model.chart.*;
import com.zygne.zchart.volumeprofile.model.chart.Canvas;
import com.zygne.zchart.volumeprofile.model.data.PriceBox;
import com.zygne.zchart.volumeprofile.model.data.Quote;
import com.zygne.zchart.volumeprofile.model.data.VolumeProfileGroup;
import com.zygne.zchart.volumeprofile.util.GroupingGenerator;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolumeProfileChart extends MouseInputAdapter implements Chart,
        GroupingMenu.Listener,
        BarSizeMenu.Listener {

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
    private int barHeight = 5;
    private String waterMarkText = "";
    private TextObject waterMark;
    private GroupingMenu groupingMenu;
    private BarSizeMenu barSizeMenu;
    private StatusBar statusBar;
    private TopBar topBar;
    private List<Object2d> objects = new ArrayList<>();
    private RendererImpl renderer;


    public VolumeProfileChart(Component component) {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);
        this.component = component;
        createButtons();

        waterMark = new TextObject(0, 0, canvasWidth, canvasHeight);
        waterMark.setFontSize(TextObject.FontSize.LARGE);
        waterMark.setText(waterMarkText);
        waterMark.setzOrder(-1);

        statusBar = new StatusBar();
        statusBar.setWidth(canvasWidth);
        statusBar.setHeight(canvasHeight);
        statusBar.setLabelWidth(labelWidth);
        statusBar.setzOrder(1);

        topBar = new TopBar();
        topBar.setX(0);
        topBar.setY(0);
        topBar.setWidth(canvasWidth);
        topBar.setHeight(canvasHeight);
        topBar.setzOrder(1);
        topBar.init();

        renderer = new RendererImpl(camera);
    }

    private void setUp() {

        if (quotes.isEmpty()) {
            return;
        }

        double maxValue = 0;
        double minValue = 0;

        Collections.sort(quotes, new Quote.PriceComparator(Quote.PriceComparator.SORT_ORDER_HIGH));
        Collections.reverse(quotes);
        maxValue = (quotes.get(0).getHigh() * 1.05);
        minValue = (quotes.get(quotes.size() - 1).getHigh() * 0.95);

        maxValue = Math.ceil(maxValue);
        minValue = Math.floor(minValue);

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
        statusBar.setGrouping(grouping);

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
            b.setX(labelWidth);
            b.setY(y);
            b.setHeight(barHeight);

            double percent = (vp.getVolume() / (double) highestValue);

            int width = (int) (percent * ((camera.getWidth()- paddingRight) - labelWidth));
            b.setWidth(width);
            b.setPercentage(percent);

            ChartUtilities.setColorScheme(b, percent);

            b.setText(String.format("%.2f", vp.getPrice()));
            b.setVolume(vp.getVolume());
            b.setzOrder(0);
            volumeProfileBars.add(b);
            y += barHeight;
            if (i % 5 == 0) {
                b.setPrintLabel(true);
            }
        }
    }

    private void createButtons() {

        groupingMenu = new GroupingMenu();
        groupingMenu.setX(camera.getWidth() - 90);
        groupingMenu.setY(30);
        groupingMenu.setWidth(60);
        groupingMenu.setHeight(120);
        groupingMenu.init();
        groupingMenu.setListener(this);
        groupingMenu.setzOrder(1);

        barSizeMenu = new BarSizeMenu();
        barSizeMenu.setX(camera.getWidth() - 90);
        barSizeMenu.setY(155);
        barSizeMenu.setWidth(60);
        barSizeMenu.setHeight(120);
        barSizeMenu.init();
        barSizeMenu.setListener(this);
        barSizeMenu.setzOrder(1);
    }

    @Override
    public void setQuotes(List<Quote> quotes) {
        this.grouping = -1;
        this.quotes.clear();
        this.quotes.addAll(quotes);
        this.volumeProfileGroups.clear();
        setUp();
        createButtons();
        createBars();
        refresh();
    }

    @Override
    public void draw(Canvas g) {
        renderer.setCanvas(g);
        renderer.Render(objects);
    }

    private void refresh() {
        objects.clear();
        objects.add(statusBar);
        objects.add(groupingMenu);
        objects.add(barSizeMenu);
        objects.add(waterMark);
        objects.add(topBar);
        objects.addAll(volumeProfileBars);
        component.repaint();
    }

    public void setGrouping(double value) {
        volumeProfileGroups.clear();
        grouping = value;
        setUp();
        createButtons();
        createBars();

        refresh();
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

        refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        topBar.setStatusText("");

        for (VolumeProfileBar b : volumeProfileBars) {
            b.highLight(false);
        }

        int x = e.getX();
        int y = e.getY();

        groupingMenu.inside(x, y);
        barSizeMenu.inside(x, y);
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void setWaterMarkText(String waterMarkText) {
        waterMark.setText(waterMarkText);
        waterMark.setColor("#003D7A");
        topBar.setLabelText(waterMarkText);
    }

    @Override
    public void onIncreaseSelected() {
        grouping = GroupingGenerator.increment(grouping);
        camera.setViewPortY(0);
        setGrouping(grouping);
    }

    @Override
    public void onDecreaseSelected() {
        grouping = GroupingGenerator.decrement(grouping);
        camera.setViewPortY(0);
        setGrouping(grouping);
    }

    @Override
    public void onBarSizeChanged(int barSize) {
        this.barHeight = barSize;
        volumeProfileGroups.clear();
        setUp();
        createButtons();
        createBars();
        camera.setViewPortY(0);
        refresh();
    }
}
