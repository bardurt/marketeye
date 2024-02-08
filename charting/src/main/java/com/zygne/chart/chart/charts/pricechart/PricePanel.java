package com.zygne.chart.chart.charts.pricechart;

import com.zygne.chart.chart.charts.ChartPanel;
import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.data.Quote;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

public class PricePanel extends ChartPanel {

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

    @Override
    public void addSeries(List<List<Serie>> series){
        priceChart.setSeries(series);
    }

    @Override
    public void addVolumeProfile(List<Quote> quotes){
        priceChart.addVolumeProfile(quotes);
    }

    @Override
    public void addWaterMark(String waterMark) {
        priceChart.setWaterMark(waterMark);
    }

    @Override
    public void addTitle(String title) {
        priceChart.setTitle(title);
    }

    private record ChartThread(Component component) implements Runnable {

        @Override
        public void run() {

            while (true) {
                EventQueue.invokeLater(component::repaint);

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
