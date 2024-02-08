package com.zygne.client.awt.components.views;

import com.zygne.chart.chart.charts.linechart.LineChart;
import com.zygne.chart.chart.charts.linechart.LinePanel;
import com.zygne.chart.chart.model.data.Quote;
import com.zygne.data.domain.model.Tendency;
import com.zygne.data.domain.model.TendencyReport;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineChart2 extends JPanel {

    private LineChart lineChart;

    public LineChart2() {
        this.setLayout(new GridLayout());
    }

    public void addTendency(String symbol, TendencyReport tendencyReport) {
        removeAll();
        List<List<Quote>> dataset = new ArrayList<List<Quote>>();
        for (Tendency t : tendencyReport.tendencies()) {

            List<Quote> quoteList = new ArrayList<>();
            for (int i = 0; i < t.data.size(); i++) {

                Quote q = new Quote();
                q.setClose(t.data.get(i).value);
                q.setTimeStamp(t.data.get(i).timeStamp);
                quoteList.add(q);
            }

            dataset.add(quoteList);
        }

        LinePanel linePanel = new LinePanel(this);
        linePanel.addQuotes(dataset);
        add(linePanel);
        invalidate();
        validate();
        repaint();
    }

}