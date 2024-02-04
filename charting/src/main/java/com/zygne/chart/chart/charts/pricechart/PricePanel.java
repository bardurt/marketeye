package com.zygne.chart.chart.charts.pricechart;

import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PricePanel extends JPanel {

    private final PriceChart priceChart;

    public PricePanel(JPanel parent){
        this.priceChart = new PriceChart();
        addMouseMotionListener(priceChart);
        addMouseListener(priceChart);

        ChartThread charThread = new ChartThread(parent);

        Thread t = new Thread(charThread);
        t.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        priceChart.draw(new AwtCanvas(g));
    }

    public void addQuotes(List<Quote> quotes){
        priceChart.setBars(quotes);
    }

    public void addVolumeProfile(List<Quote> quotes){
        priceChart.addVolumeProfile(quotes);
    }

    public void addWaterMark(String waterMark) {
        priceChart.setWaterMark(waterMark);
    }

    public void addTitle(String waterMark) {
        priceChart.setTitle(waterMark);
    }

    private static class ChartThread implements Runnable{

        private Component component;

        public ChartThread(Component component) {
            this.component = component;
        }

        @Override
        public void run() {

            while (true){
                EventQueue.invokeLater(() -> component.repaint());

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
