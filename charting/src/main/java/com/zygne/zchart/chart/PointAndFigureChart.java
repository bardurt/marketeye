package com.zygne.zchart.chart;

import com.zygne.zchart.chart.menu.PriceBoard;
import com.zygne.zchart.chart.menu.TopBar;
import com.zygne.zchart.chart.model.chart.Camera;
import com.zygne.zchart.chart.model.chart.Canvas;
import com.zygne.zchart.chart.model.chart.Object2d;
import com.zygne.zchart.chart.model.data.PnfItem;
import com.zygne.zchart.chart.model.data.PointAndFigure;
import com.zygne.zchart.chart.model.data.Quote;
import com.zygne.zchart.chart.util.PnFCalc;
import com.zygne.zchart.chart.util.PointAndFigureUtil;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PointAndFigureChart extends MouseInputAdapter implements Chart {

    private static final int DEFAULT_HEIGHT = 640;
    private static final int DEFAULT_WIDTH = 1080;

    private Component component;
    private int startY;
    private int startX;
    private int canvasHeight = DEFAULT_HEIGHT;
    private int canvasWidth = DEFAULT_WIDTH;
    private Camera camera;
    private TopBar topBar;
    private List<Object2d> objects = new ArrayList<>();
    private RendererImpl renderer;
    private PriceBoard priceBoard;
    private List<Quote> quotes = new ArrayList<>();
    private PnFCalc pnFCalc = new PnFCalc();


    public PointAndFigureChart(Component component) {
        this.camera = new Camera(0, 0);
        this.camera.setX(0);
        this.camera.setY(0);
        this.camera.setHeight(canvasHeight);
        this.camera.setWidth(canvasWidth);
        this.component = component;

        topBar = new TopBar();
        topBar.setX(0);
        topBar.setY(0);
        topBar.setWidth(canvasWidth);
        topBar.setHeight(canvasHeight);
        topBar.setzOrder(1);
        topBar.init();

        renderer = new RendererImpl(camera);

        priceBoard = new PriceBoard(100, 0, 1);
        priceBoard.setWidth(canvasWidth);
        priceBoard.setHeight(canvasHeight);
        priceBoard.setX(0);
        priceBoard.setY(0);
        priceBoard.init();
        objects.addAll(priceBoard.getObject2dList());
    }


    @Override
    public void draw(Canvas g) {
        renderer.setCanvas(g);
        renderer.Render(objects);
    }

    private void refresh() {
        component.repaint();
    }

    @Override
    public void setBars(List<Quote> bars) {
        objects.clear();
        this.quotes.clear();
        this.quotes.addAll(bars);

        Collections.sort(this.quotes, new Quote.PriceComparator(Quote.PriceComparator.SORT_ORDER_HIGH));
        Collections.reverse(this.quotes);

        double maxValue = (this.quotes.get(0).getHigh() * 1.05);
        double minValue = (this.quotes.get(bars.size() - 1).getHigh() * 0.95);

        Collections.sort(this.quotes, new Quote.TimeComparator());

        double priceChange = this.quotes.get(bars.size() - 1).getClose() * 0.01;
        double boxSize = PnFCalc.getBoxSize(priceChange);

        priceBoard = new PriceBoard(maxValue, minValue, boxSize);
        priceBoard.setWidth(canvasWidth);
        priceBoard.setHeight(canvasHeight);
        priceBoard.setX(0);
        priceBoard.setY(0);
        priceBoard.init();
        objects.addAll(priceBoard.getObject2dList());

        List<PnfItem> items = pnFCalc.execute(this.quotes);

        List<PointAndFigure> pnf = PointAndFigureUtil.createPnF2(items, boxSize);
        PointAndFigureUtil.adjustPnfToPriceBoard(pnf, priceBoard);
        objects.addAll(pnf);
//        List<PointAndFigureBox> boxes = PointAndFigureUtil.createPnfBoxes(pnf, boxSize);
//
//        for (PointAndFigureBox e : boxes) {
//            PointAndFigureUtil.adjustPnfToPriceBoard(e.getItems(), priceBoard);
//            objects.addAll(e.getItems());
//        }


        camera.setViewPortY(0);
        camera.setViewPortX(0);
        refresh();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        startX = e.getX();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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

        int dx = startX - e.getY();
        int x = (int) (camera.getViewPortX() - (dx / 100));
        camera.setViewPortY(y);
        camera.setViewPortX(x);
        priceBoard.setX(-camera.getViewPortX());
        refresh();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
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
