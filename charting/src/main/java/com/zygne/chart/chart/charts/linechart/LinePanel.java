package com.zygne.chart.chart.charts.linechart;
import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LinePanel extends JPanel {

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

    public void addQuotes(List<List<Quote>> quotes){
        lineChart.setSeries(quotes);
    }


    public void addWaterMark(String waterMark) {
        lineChart.setWaterMark(waterMark);
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