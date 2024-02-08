package com.zygne.client.swing.components.views;

import com.zygne.data.domain.model.TendencyReport;

import javax.swing.*;
import java.awt.*;

public class DualChartView  extends JPanel {

    private final LineChart2 leftChart;
    private final LineChart2 rightChart;

    public DualChartView(){
        setLayout(new GridLayout(0, 2));

        leftChart = new LineChart2();
        rightChart = new LineChart2();

        add(leftChart);
        add(rightChart);

    }

    public void setLeft(String symbol, TendencyReport tendencyReport) {
        leftChart.addTendency(symbol, tendencyReport);
        invalidate();
        validate();
        repaint();
    }
    public void setRightChart(String symbol, TendencyReport tendencyReport) {
        rightChart.addTendency(symbol, tendencyReport);
        invalidate();
        validate();
        repaint();
    }

}
