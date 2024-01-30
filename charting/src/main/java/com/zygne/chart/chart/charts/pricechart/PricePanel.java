package com.zygne.chart.chart.charts.pricechart;

import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PricePanel extends JPanel {

    private PriceChart priceChart;
    private JPanel parent;
    private CharThread charThread;

    public PricePanel(JPanel parent){
        this.parent = parent;
        this.priceChart = new PriceChart();
        addMouseMotionListener(priceChart);
        addMouseListener(priceChart);

        charThread = new CharThread(parent);

        Thread t = new Thread(charThread);
        t.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background
        priceChart.draw(new AwtCanvas(g));
    }

    public void addQuotes(List<Quote> quotes){
        priceChart.setBars(quotes);
    }

    public void addVolumeProfile(List<Quote> quotes){
        priceChart.addVolumeProfile(quotes);
    }

    public void addPriceGaps(List<Quote> quotes){
        priceChart.addPriceGaps(quotes);
    }

    public void addPriceImbalances(List<Quote> quotes){
        priceChart.addPriceImbalances(quotes);
    }

    public void addPricePressure(List<Quote> quotes){
        priceChart.addPricePressure(quotes);
    }

    public void setCurrentPrice(double price){
        priceChart.setCurrentPrice(price);
    }

    public void addWaterMark(String waterMark) {
        priceChart.setWaterMark(waterMark);
    }

    public void addTitle(String waterMark) {
        priceChart.setTitle(waterMark);
    }

    private class CharThread implements Runnable{

        private Component component;

        public CharThread(Component component) {
            this.component = component;
        }

        @Override
        public void run() {

            while (true){
                component.repaint();

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
