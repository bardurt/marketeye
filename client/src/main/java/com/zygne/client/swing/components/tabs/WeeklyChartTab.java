package com.zygne.client.swing.components.tabs;

import com.zygne.client.swing.components.views.PriceChartView;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class WeeklyChartTab extends JPanel {

    public final PriceChartView priceChartView;

    public WeeklyChartTab() {
        super();
        setLayout(new BorderLayout());

        priceChartView = new PriceChartView();

        JPanel dataSourceView = new JPanel(new BorderLayout());
        dataSourceView.add(priceChartView);

        add(dataSourceView, BorderLayout.CENTER);
    }

}