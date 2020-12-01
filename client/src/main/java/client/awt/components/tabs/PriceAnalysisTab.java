package client.awt.components.tabs;

import client.awt.components.tables.LiquidityLevelRenderer;
import client.awt.components.tables.LiquidityLevelTableModel;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PriceAnalysisTab extends JPanel {

    private LiquidityLevelTableModel tableModelResistance;
    private LiquidityLevelTableModel tableModelSupport;
    private JTable tableResistance;
    private JTable tableSupport;
    private JScrollPane scrollResistance;
    private JScrollPane scrollSupport;

    public PriceAnalysisTab(){
        setLayout(new GridLayout(1,1));

        JPanel resistancePanel = new JPanel(new BorderLayout());

        resistancePanel.add(new JLabel("Resistance"), BorderLayout.NORTH);

        tableModelResistance = new LiquidityLevelTableModel();
        tableResistance = new JTable(tableModelResistance);
        tableResistance.setDefaultRenderer(LiquidityLevel.class, new LiquidityLevelRenderer());
        tableResistance.setDefaultRenderer(String.class, new LiquidityLevelRenderer());

        scrollResistance = new JScrollPane(tableResistance);
        scrollResistance.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        resistancePanel.add(scrollResistance, BorderLayout.CENTER);
        add(resistancePanel);

        JPanel supportPanel = new JPanel(new BorderLayout());
        supportPanel.add(new JLabel("Support"), BorderLayout.NORTH);

        tableModelSupport = new LiquidityLevelTableModel();
        tableSupport = new JTable(tableModelSupport);
        tableSupport.setDefaultRenderer(LiquidityLevel.class, new LiquidityLevelRenderer());
        tableSupport.setDefaultRenderer(String.class, new LiquidityLevelRenderer());

        scrollSupport = new JScrollPane(tableSupport);
        scrollSupport.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        supportPanel.add(scrollSupport, BorderLayout.CENTER);

        add(supportPanel);
    }

    public void addResistance(List<LiquidityLevel> data){
        if (tableResistance != null) {
            tableModelResistance.clear();
            tableModelResistance.addItems(data);
            tableModelResistance.fireTableDataChanged();
            scrollResistance.invalidate();
        }
    }

    public void addSupport(List<LiquidityLevel> data){
        if (tableSupport != null) {
            tableModelSupport.clear();
            tableModelSupport.addItems(data);
            tableModelSupport.fireTableDataChanged();
            scrollSupport.invalidate();
        }
    }

    public void reset(){

    }

}
