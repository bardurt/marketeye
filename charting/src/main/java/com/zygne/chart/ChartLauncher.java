package com.zygne.chart;

import com.zygne.chart.chart.charts.bar.BarChart;
import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartLauncher extends JPanel {

    private BarChart barChart;

    public ChartLauncher() {
        super(new BorderLayout());
        setSize(880, 880);
        barChart = new BarChart();
        add(barChart, BorderLayout.CENTER);
        populate();

        JFrame frame = new JFrame("Chart Laumcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(1024, 512));
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        barChart.draw(new AwtCanvas(g));
        System.out.println("Barchart draw");
    }

    private void populate(){

        java.util.List<List<Serie>> data = new ArrayList<>();
        java.util.List<Serie> serieList = new ArrayList<>();
        serieList.add(new BarSerie(10d));
        serieList.add(new BarSerie(15d));
        serieList.add(new BarSerie(16d));
        serieList.add(new BarSerie(15d));
        serieList.add(new BarSerie(16d));
        serieList.add(new BarSerie(10d));
        serieList.add(new BarSerie(5d));
        serieList.add(new BarSerie(2d));
        serieList.add(new BarSerie(-1d));
        serieList.add(new BarSerie(-20d));

        data.add(serieList);
        barChart.setSeries(data);

    }

    public static void main(String[] args) {
        new ChartLauncher();
    }

}
