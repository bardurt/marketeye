package com.zygne.client.swing.components.views;

import com.zygne.chart.chart.charts.linechart.LineChart;
import com.zygne.chart.chart.model.data.LineSerie;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.data.domain.model.Tendency;
import com.zygne.data.domain.model.TendencyReport;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineChart2 extends JPanel {

    public LineChart2() {
        this.setLayout(new GridLayout());
    }

    public void addTendency(String symbol, TendencyReport tendencyReport) {
        removeAll();
        List<List<Serie>> dataset = new ArrayList<>();
        for (Tendency t : tendencyReport.tendencies()) {
            String name = t.name;
            List<Serie> quoteList = new ArrayList<>();
            for (int i = 0; i < t.data.size(); i++) {

                LineSerie item = new LineSerie();
                item.setY(t.data.get(i).value);
                item.setTimeStamp(t.data.get(i).timeStamp);
                item.setName(name);
                quoteList.add(item);
            }
            dataset.add(quoteList);
        }

        LineChart linePanel = new LineChart();
        linePanel.setTitle(symbol);
        linePanel.setSeries(dataset);
        linePanel.setWaterMark(symbol);

        add(linePanel);
        invalidate();
        validate();
        repaint();
    }

}