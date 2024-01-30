package client.awt.components.views;

import com.zygne.stockanalyzer.domain.model.TendencyReport;

import javax.swing.*;
import java.awt.*;

public class DualChartView  extends JPanel {

    private LineChart leftChart;
    private LineChart rightChart;

    public DualChartView(){
        setLayout(new GridLayout(0, 2));

        leftChart = new LineChart();
        rightChart = new LineChart();

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
