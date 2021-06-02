package client.awt.components.tabs;

import client.awt.components.tables.LiquidityLevelRenderer;
import client.awt.components.tables.LiquidityLevelTableModel;
import client.awt.components.views.FundamentalsView;
import com.zygne.zchart.volumeprofile.VolumeProfilePanel;
import com.zygne.zchart.volumeprofile.model.data.Quote;
import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VpaTab extends JPanel{

    private LiquidityLevelTableModel tableModelPrices;
    private JTable tablePrices;

    private FundamentalsView fundamentalsView;

    private VolumeProfilePanel vpGrouped;

    public VpaTab() {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());

        fundamentalsView = new FundamentalsView();

        contentPanel.add(fundamentalsView, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel(new BorderLayout());

        JPanel supplyPanel = new JPanel(new BorderLayout());
        supplyPanel.add(new JLabel("Volume at price"), BorderLayout.NORTH);
        JPanel pricesPanel = new JPanel(new GridLayout(0,1));

        tableModelPrices = new LiquidityLevelTableModel();
        tablePrices = new JTable(tableModelPrices);
        tablePrices.setDefaultRenderer(LiquidityLevel.class, new LiquidityLevelRenderer());
        tablePrices.setDefaultRenderer(String.class, new LiquidityLevelRenderer());
        tablePrices.setRowSorter(tableModelPrices.getSorter(tablePrices.getModel()));

        supplyPanel.add(new JScrollPane(tablePrices), BorderLayout.CENTER);
        pricesPanel.add(supplyPanel);

        tablesPanel.add(pricesPanel, BorderLayout.WEST);

        JPanel volumePanel = new JPanel(new BorderLayout());
        volumePanel.add(new JLabel("Volume Profile"), BorderLayout.NORTH);

        vpGrouped = new VolumeProfilePanel(volumePanel, VolumeProfilePanel.ChartType.GROUPED);
        volumePanel.add(vpGrouped, BorderLayout.CENTER);

        tablesPanel.add(volumePanel, BorderLayout.CENTER);

        contentPanel.add(tablesPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void addSupply(List<LiquidityLevel> data){
        fundamentalsView.clear();
        if (tablePrices != null) {
            tableModelPrices.clear();
            data.sort(new LiquidityLevel.PriceComparator());
            Collections.reverse(data);
            tableModelPrices.addItems(data);
            tableModelPrices.fireTableDataChanged();
            tablePrices.invalidate();

            for(int i = 0; i < tablePrices.getModel().getColumnCount(); i++){
                tablePrices.convertRowIndexToView(i);
            }
        }

    }

    public void addVolumeProfile(String symbol, java.util.List<LiquidityLevel> levels){
        List<Quote> volumeProfileList = new ArrayList<Quote>();

        for(LiquidityLevel e : levels){

            Quote quote = new Quote();
            quote.setHigh(e.price);
            quote.setVolume(e.getVolume());
            volumeProfileList.add(quote);
        }
        vpGrouped.addQuotes(volumeProfileList);
        vpGrouped.addWaterMark(symbol);
    }

    public void addBinnedSupply(List<LiquidityLevel> data){

    }

    public void addFundamentals(Fundamentals fundamentals){
        fundamentalsView.populateFrom(fundamentals);
    }

}
