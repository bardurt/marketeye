package com.zygne.client.swing.components.tabs;

import com.zygne.client.swing.components.views.PriceChartView;
import com.zygne.client.swing.components.views.StocksView;

import javax.swing.*;
import java.awt.*;

public class ChartTab extends JPanel implements StocksView.Callback {

    private final Callback callback;
    private final StocksView reportView;
    public final PriceChartView priceChartView;

    public ChartTab(Callback callback) {
        super();
        this.callback = callback;
        setLayout(new BorderLayout());

        reportView = new StocksView();
        reportView.setCallback(this);
        priceChartView = new PriceChartView();

        JPanel dataSourceView = new JPanel(new BorderLayout());
        dataSourceView.add(priceChartView);

        add(reportView, BorderLayout.NORTH);
        add(dataSourceView, BorderLayout.CENTER);
    }

    public interface Callback {
        void generateReport(String symbol);
    }

    @Override
    public void reportButtonClicked(String symbol) {
        if (callback != null) {
            callback.generateReport(symbol);
        }
    }

}