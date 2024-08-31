package com.zygne.client.swing.components.tabs;

import com.zygne.chart.chart.charts.bar.BarChart;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.data.domain.model.Bias;
import com.zygne.data.domain.model.CotData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CotTab extends JPanel {

    private final BarChart barChartRaw;

    public CotTab() {
        setLayout(new BorderLayout());
        JPanel barLayout = new JPanel(new GridLayout(1, 0));

        barChartRaw = new BarChart();


        barLayout.add(barChartRaw);

        add(barLayout, BorderLayout.CENTER);
    }

    public void setCotData(List<CotData> cotData) {
        barChartRaw.setSeries(adjustedToMonths(18, cotData));
    }


    private List<List<Serie>> adjustedToMonths(int months, List<CotData> cotData) {

        List<List<Serie>> series = new ArrayList<>();
        List<Serie> serieList = new ArrayList<>();

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        int start = cotData.size() - (months * 5);

        for (int i = start; i < cotData.size(); i++) {
            CotData c = cotData.get(i);

            if (c.getNet() > max) {
                max = c.getNet();
            }

            if (c.getNet() < min) {
                min = c.getNet();
            }
        }

        double mid = (max + min) / 2;

        for (int i = 0; i < cotData.size(); i++) {
            CotData c = cotData.get(i);
            BarSerie serie = new BarSerie((c.getNet() - mid) / 1000);
            if (i >= start) {
                serie.setIncluded(true);
                serie.setTimeStamp(c.getTimeStamp());
                serieList.add(serie);
            }
        }

        series.add(serieList);

        return series;
    }

}
