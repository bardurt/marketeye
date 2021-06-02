package com.zygne.zchart.volumeprofile;

import com.zygne.zchart.volumeprofile.model.chart.AwtCanvas;
import com.zygne.zchart.volumeprofile.model.chart.Canvas;
import com.zygne.zchart.volumeprofile.model.data.Quote;

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

    public void addQuotes(List<Quote> quotes){
        volumeProfileChart.setQuotes(quotes);
        parent.repaint();
    }

    public void addWaterMark(String waterMark) {
        volumeProfileChart.setWaterMarkText(waterMark);
        parent.repaint();
    }

    public enum ChartType {
        RAW,
        GROUPED
    }
}
