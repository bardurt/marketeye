package com.zygne.client.swing.components.tabs;

import com.zygne.client.swing.components.views.FuturesView;
import com.zygne.client.swing.components.views.SingleChartView;
import com.zygne.data.domain.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TendencyTab extends JPanel implements FuturesView.Callback {

    private final Callback callback;
    private final FuturesView reportView;
    private final SingleChartView singleChartView;
    private String symbol;

    public TendencyTab(Callback callback) {
        super();
        this.callback = callback;
        setLayout(new BorderLayout());

        singleChartView = new SingleChartView();

        reportView = new FuturesView();
        reportView.setCallback(this);

        JPanel chartPanel = new JPanel(new GridLayout(0, 1));
        chartPanel.add(singleChartView);

        add(reportView, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
    }

    public void setAssets(List<Asset> data) {
        reportView.setAssets(data);
    }

    @Override
    public void reportButtonClicked(String name, String symbol) {
        if (callback != null) {
            this.symbol = name;
            callback.generateTendency(symbol);
        }
    }

    public void addTendency(TendencyReport tendencyReport) {
        singleChartView.addTendency(symbol, tendencyReport);
    }

    public interface Callback {
        void generateTendency(String symbol);
    }
}