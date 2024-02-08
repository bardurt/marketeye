package com.zygne.chart.chart.charts;

import com.zygne.chart.chart.model.data.Quote;
import com.zygne.chart.chart.model.data.Serie;

import javax.swing.*;
import java.util.List;

public abstract class ChartPanel extends JPanel {

    public abstract void addSeries(List<List<Serie>> series);

    public abstract void addVolumeProfile(List<Quote> quotes);

    public abstract void addWaterMark(String waterMark);

    public abstract void addTitle(String title);
}
