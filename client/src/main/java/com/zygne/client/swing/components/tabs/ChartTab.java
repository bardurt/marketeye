package com.zygne.client.swing.components.tabs;

import com.zygne.client.swing.components.views.PriceChartView;

import javax.swing.*;
import java.awt.*;

public class ChartTab extends JPanel {

    public final PriceChartView priceChartView;

    public ChartTab() {
        super();
        setLayout(new BorderLayout());

        priceChartView = new PriceChartView();

        JPanel dataSourceView = new JPanel(new BorderLayout());
        dataSourceView.add(priceChartView);

        add(dataSourceView, BorderLayout.CENTER);
    }

}