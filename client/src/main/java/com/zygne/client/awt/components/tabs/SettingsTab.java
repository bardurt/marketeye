package com.zygne.client.awt.components.tabs;

import com.zygne.client.awt.ResourceLoader;
import com.zygne.client.awt.components.views.PriceChartView;
import com.zygne.client.awt.components.views.StocksView;

import javax.swing.*;
import java.awt.*;

public class SettingsTab extends JPanel implements StocksView.Callback {

    private final Callback callback;
    private final StocksView reportView;
    public final PriceChartView priceChartView;

    public SettingsTab(Callback callback) {
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