package com.zygne.client.swing.components.views;

import com.zygne.data.domain.model.TendencyReport;

import javax.swing.*;
import java.awt.*;

public class SingleChartView  extends JPanel {

    private LineChart2 lineChart2;

    public SingleChartView(){
        setLayout(new GridLayout());

        lineChart2 = new LineChart2();

        add(lineChart2);
    }

    public void addTendency(String symbol, TendencyReport tendencyReport) {
        lineChart2.addTendency(symbol, tendencyReport);
        invalidate();
        validate();
        repaint();
    }

}
