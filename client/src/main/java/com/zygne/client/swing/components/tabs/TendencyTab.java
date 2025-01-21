package com.zygne.client.swing.components.tabs;

import com.zygne.chart.chart.charts.linechart.LineChart;
import com.zygne.chart.chart.model.data.LineSerie;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.data.domain.model.*;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class TendencyTab extends JPanel {


    private final LineChart linePanel;

    public TendencyTab() {
        super();
        setLayout(new BorderLayout());
        linePanel = new LineChart();
        add(linePanel);
    }

    public void addTendency(TendencyReport tendencyReport, String title) {
        java.util.List<java.util.List<Serie>> dataset = new ArrayList<>();
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

        linePanel.setWaterMark(title);
        linePanel.setSeries(dataset);
    }

}