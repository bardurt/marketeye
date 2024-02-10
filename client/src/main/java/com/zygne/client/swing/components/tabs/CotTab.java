package com.zygne.client.swing.components.tabs;

import com.zygne.chart.chart.charts.bar.BarChart;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.data.domain.model.CotData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CotTab extends JPanel {

    private final BarChart barChartRaw;
    private final BarChart barChartAdjusted;
    private final BarChart barChartAdjusted3Months;

    public CotTab() {
        setLayout(new BorderLayout());
        JPanel barLayout = new JPanel(new GridLayout(3, 0));

        barChartRaw = new BarChart();
        barChartAdjusted = new BarChart();
        barChartAdjusted3Months = new BarChart();

        barLayout.add(barChartRaw);
        barLayout.add(barChartAdjusted);
        barLayout.add(barChartAdjusted3Months);

        add(barLayout, BorderLayout.CENTER);
    }

    public void setCotData(List<CotData> cotData) {
        barChartRaw.setSeries(adjustedToMonths(12, cotData));
        barChartAdjusted.setSeries(adjustedToMonths(6, cotData));
        barChartAdjusted3Months.setSeries(adjustedToMonths(3, cotData));
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

        for (CotData c : cotData) {
            BarSerie serie =new BarSerie((c.getNet() -mid)/ 1000);
            serie.setTimeStamp(c.getTimeStamp());
            serieList.add(serie);
        }

        series.add(serieList);

        return series;
    }

}
