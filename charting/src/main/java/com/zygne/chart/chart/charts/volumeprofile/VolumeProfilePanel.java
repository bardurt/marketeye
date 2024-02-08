package com.zygne.chart.chart.charts.volumeprofile;

import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VolumeProfilePanel extends JPanel {

    private VolumeProfileChart volumeProfileChart;
    private JPanel parent;

    public VolumeProfilePanel(JPanel parent, ChartType chartType) {
        this.parent = parent;
        this.volumeProfileChart = new VolumeProfileChart(this);
        addMouseMotionListener(volumeProfileChart);
        addMouseListener(volumeProfileChart);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background
        volumeProfileChart.draw(new AwtCanvas(g));
    }

    public void addQuotes(List<List<Quote>> quotes){
        volumeProfileChart.setSeries(quotes);
        parent.repaint();
    }

    public void setCurrentPrice(double price){
        volumeProfileChart.setCurrentPrice(price);
        parent.repaint();
    }

    public void addWaterMark(String waterMark) {
        volumeProfileChart.setWaterMark(waterMark);
        parent.repaint();
    }

    public void addTitle(String waterMark) {
        volumeProfileChart.setTitle(waterMark);
        parent.repaint();
    }

    public enum ChartType {
        RAW,
        GROUPED
    }
}
