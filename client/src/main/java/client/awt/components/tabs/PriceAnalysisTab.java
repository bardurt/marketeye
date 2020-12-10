package client.awt.components.tabs;

import client.awt.components.tables.LiquidityLevelRenderer;
import client.awt.components.tables.LiquidityLevelTableModel;
import client.awt.components.views.FundamentalsView;
import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PriceAnalysisTab extends JPanel {

    private LiquidityLevelTableModel tableModelResistance;
    private JTable tableResistance;
    private JScrollPane scrollResistance;
    private FundamentalsView fundamentalsView;

    public PriceAnalysisTab(){
        setLayout(new BorderLayout());

        fundamentalsView = new FundamentalsView();

        add(fundamentalsView, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel(new GridLayout(1,1));
        JPanel resistancePanel = new JPanel(new BorderLayout());

        resistancePanel.add(new JLabel("Resistance"), BorderLayout.NORTH);

        tableModelResistance = new LiquidityLevelTableModel();
        tableResistance = new JTable(tableModelResistance);
        tableResistance.setDefaultRenderer(LiquidityLevel.class, new LiquidityLevelRenderer());
        tableResistance.setDefaultRenderer(String.class, new LiquidityLevelRenderer());

        scrollResistance = new JScrollPane(tableResistance);
        scrollResistance.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        resistancePanel.add(scrollResistance, BorderLayout.CENTER);
        tablesPanel.add(resistancePanel);

        add(tablesPanel, BorderLayout.CENTER);
    }

    public void addResistance(List<LiquidityLevel> data){
        fundamentalsView.clear();
        if (tableResistance != null) {
            tableModelResistance.clear();
            tableModelResistance.addItems(data);
            tableModelResistance.fireTableDataChanged();
            scrollResistance.invalidate();
            tableResistance.invalidate();
        }
    }

    public void addSupport(List<LiquidityLevel> data){
    }

    public void addFundamentals(Fundamentals fundamentals){
        fundamentalsView.populateFrom(fundamentals);
    }

    public void reset(){
        fundamentalsView.clear();
    }


}
