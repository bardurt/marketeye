package client.awt.components.tabs;

import client.awt.components.tables.LiquidityLevelRenderer;
import client.awt.components.tables.LiquidityLevelTableModel;
import client.awt.components.views.FundamentalsView;
import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class PriceAnalysisTab extends JPanel {

    private LiquidityLevelTableModel tableModelPrices;
    private LiquidityLevelTableModel tableModelVolume;

    private JTable tablePrices;
    private JTable tableVolume;

    private FundamentalsView fundamentalsView;



    public PriceAnalysisTab(){
        setLayout(new BorderLayout());

        fundamentalsView = new FundamentalsView();

        add(fundamentalsView, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel(new GridLayout(1,2));;

        tableModelPrices = new LiquidityLevelTableModel();
        tablePrices = new JTable(tableModelPrices);
        tablePrices.setDefaultRenderer(LiquidityLevel.class, new LiquidityLevelRenderer());
        tablePrices.setDefaultRenderer(String.class, new LiquidityLevelRenderer());


        tablesPanel.add(new JScrollPane(tablePrices));

        tableModelVolume = new LiquidityLevelTableModel();
        tableVolume = new JTable(tableModelVolume);
        tableVolume.setDefaultRenderer(LiquidityLevel.class, new LiquidityLevelRenderer());
        tableVolume.setDefaultRenderer(String.class, new LiquidityLevelRenderer());

        tablesPanel.add(new JScrollPane(tableVolume));

        add(tablesPanel, BorderLayout.CENTER);
    }



}
