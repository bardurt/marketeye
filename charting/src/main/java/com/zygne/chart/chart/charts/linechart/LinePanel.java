package com.zygne.chart.chart.charts.linechart;
import com.zygne.chart.chart.charts.ChartPanel;
import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.data.Quote;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LinePanel extends ChartPanel {

    private final LineChart lineChart;

    public LinePanel(JPanel parent){
        this.lineChart = new LineChart();
        addMouseMotionListener(lineChart);
        addMouseListener(lineChart);

        ChartThread charThread = new ChartThread(parent);

        Thread t = new Thread(charThread);
        t.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        lineChart.draw(new AwtCanvas(g));
    }

    @Override
    public void addSeries(List<List<Serie>> series){
        lineChart.setSeries(series);
    }

    @Override
    public void addWaterMark(String waterMark) {
        lineChart.setWaterMark(waterMark);
    }

    @Override
    public void addVolumeProfile(List<Quote> quotes) {

    }

    @Override
    public void addTitle(String title) {
        lineChart.setTitle(title);
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