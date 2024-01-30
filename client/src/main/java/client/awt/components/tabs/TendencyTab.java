package client.awt.components.tabs;

import client.awt.components.views.DualChartView;
import client.awt.components.views.FuturesView;
import client.awt.components.views.SingleChartView;
import com.zygne.stockanalyzer.domain.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TendencyTab extends JPanel implements FuturesView.Callback {

    private Callback callback;
    private FuturesView reportView;
    private SingleChartView singleChartView;
    private DualChartView dualChartView;
    private String symbol;


    private JPanel chartPanel;
    private int currentChart;
    private boolean dualChart = false;

    public TendencyTab(Callback callback) {
        super();
        this.callback = callback;
        setLayout(new BorderLayout());

        singleChartView = new SingleChartView();
        dualChartView = new DualChartView();

        reportView = new FuturesView();
        reportView.setCallback(this);

        chartPanel = new JPanel(new GridLayout(0, 1));
        chartPanel.add(singleChartView);


        add(reportView, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);

    }

    public void setAssets(List<Asset> data, int defaultSelection) {
        reportView.setAssets(data, defaultSelection);
    }

    @Override
    public void reportButtonClicked(String name, String symbol) {
        if (callback != null) {
            this.symbol = name;
            callback.generateTendency(symbol);
        }
    }

    @Override
    public void toggleDualChart(boolean active) {
        dualChart = active;
        System.out.println("Dual chart " + dualChart);

        if (dualChart) {
            chartPanel.remove(singleChartView);
            chartPanel.add(dualChartView);
        } else {
            chartPanel.remove(dualChartView);
            chartPanel.add(singleChartView);
        }

        invalidate();
        validate();
        repaint();
    }

    @Override
    public void onChartSelected(int chart) {
        System.out.println("Chart updated " + chart);
        currentChart = chart;
    }

    public void addTendency(TendencyReport tendencyReport) {

        if (dualChart) {
            System.out.println("Adding data to chart " + currentChart);
            if (currentChart == 0) {
                dualChartView.setLeft(symbol, tendencyReport);
            } else {
                dualChartView.setRightChart(symbol, tendencyReport);
            }
        } else {
            singleChartView.addTendency(symbol, tendencyReport);
        }

        invalidate();
        repaint();
    }

    public interface Callback {
        void generateTendency(String symbol);
    }
}