package com.zygne.client.awt.components.views;

import com.zygne.data.domain.model.TendencyReport;

import javax.swing.*;
import java.awt.*;

public class SingleChartView  extends JPanel {

    private LineChart lineChart;

    public SingleChartView(){
        setLayout(new GridLayout());

        lineChart = new LineChart();

        add(lineChart);
    }

    public void addTendency(String symbol, TendencyReport tendencyReport) {
        lineChart.addTendency(symbol, tendencyReport);
        invalidate();
        validate();
        repaint();
    }

}
