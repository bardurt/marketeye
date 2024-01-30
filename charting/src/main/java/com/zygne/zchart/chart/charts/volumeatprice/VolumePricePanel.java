package com.zygne.zchart.chart.charts.volumeatprice;

import com.zygne.zchart.chart.model.chart.AwtCanvas;
import com.zygne.zchart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VolumePricePanel extends JPanel {

    private VolumePriceChart volumePriceChart;
    private JPanel parent;

    public VolumePricePanel(JPanel parent){
        this.parent = parent;
        this.volumePriceChart = new VolumePriceChart(this);
        addMouseMotionListener(volumePriceChart);
        addMouseListener(volumePriceChart);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background
        volumePriceChart.draw(new AwtCanvas(g));
    }

    public void addQuotes(List<Quote> quotes){
        volumePriceChart.setBars(quotes);
        parent.repaint();
    }

    public void setCurrentPrice(double price){
        volumePriceChart.setCurrentPrice(price);
        parent.repaint();
    }

    public void addWaterMark(String waterMark) {
        volumePriceChart.setWaterMark(waterMark);
        parent.repaint();
    }

    public void addTitle(String waterMark) {
        volumePriceChart.setTitle(waterMark);
        parent.repaint();
    }
}
