package client.awt.components.tabs;

import client.awt.components.tables.LiquiditySideRenderer;
import client.awt.components.tables.LiquiditySideTableModel;
import client.awt.components.tables.PriceGapRenderer;
import client.awt.components.tables.PriceGapTableModel;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.PriceGap;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class LiquiditySideTab extends JPanel {

    private LiquiditySideTableModel dailySellSideModel;
    private JTable dailySellSideTable;

    private LiquiditySideTableModel dailyBuySideModel;
    private JTable dailyBuySideTable;

    private JTable priceGapTableDaily;
    private PriceGapTableModel pricegapTableModelDaily;

    private JTable priceGapTableIntraDay;
    private PriceGapTableModel pricegapTableModelIntraDay;

    public LiquiditySideTab() {
        setLayout(new GridLayout(2,1));

        dailySellSideModel = new LiquiditySideTableModel();
        dailySellSideTable = new JTable(dailySellSideModel);
        dailySellSideTable.setDefaultRenderer(String.class, new LiquiditySideRenderer());
        dailySellSideTable.setRowSorter(dailySellSideModel.getSorter(dailySellSideTable.getModel()));

        dailyBuySideModel = new LiquiditySideTableModel();
        dailyBuySideTable = new JTable(dailyBuySideModel);
        dailyBuySideTable.setDefaultRenderer(String.class, new LiquiditySideRenderer());
        dailyBuySideTable.setRowSorter(dailyBuySideModel.getSorter(dailyBuySideTable.getModel()));

        pricegapTableModelDaily = new PriceGapTableModel();

        priceGapTableDaily = new JTable(pricegapTableModelDaily);
        priceGapTableDaily.setDefaultRenderer(String.class, new PriceGapRenderer());

        pricegapTableModelIntraDay = new PriceGapTableModel();

        priceGapTableIntraDay = new JTable(pricegapTableModelIntraDay);
        priceGapTableIntraDay.setDefaultRenderer(String.class, new PriceGapRenderer());

        JPanel dailyPanel = new JPanel(new BorderLayout());

        JPanel dailyContainer = new JPanel(new GridLayout(0,2));

        JPanel dailyBuy = new JPanel(new BorderLayout());
        dailyBuy.add(new JLabel("Buy Side"), BorderLayout.NORTH);
        dailyBuy.add(new JScrollPane(dailyBuySideTable), BorderLayout.CENTER);

        dailyContainer.add(dailyBuy);

        JPanel dailySell = new JPanel(new BorderLayout());
        dailySell.add(new JLabel("Sell Side"), BorderLayout.NORTH);
        dailySell.add(new JScrollPane(dailySellSideTable), BorderLayout.CENTER);

        dailyContainer.add(dailySell);
        dailyPanel.add(dailyContainer);

        add(dailyPanel);

        JPanel priceGapsPanel = new JPanel(new GridLayout());

        JPanel priceGapsDailyPanel = new JPanel(new BorderLayout());
        priceGapsDailyPanel.add(new JLabel("Price Gaps Daily"), BorderLayout.NORTH);
        priceGapsDailyPanel.add(new JScrollPane(priceGapTableDaily), BorderLayout.CENTER);

        JPanel priceGapsIntraDayPanel = new JPanel(new BorderLayout());
        priceGapsIntraDayPanel.add(new JLabel("Price Gaps Intraday"), BorderLayout.NORTH);
        priceGapsIntraDayPanel.add(new JScrollPane(priceGapTableIntraDay), BorderLayout.CENTER);

        priceGapsPanel.add(priceGapsDailyPanel);
        priceGapsPanel.add(priceGapsIntraDayPanel);

        add(priceGapsPanel);
    }

    public void addDaily(List<LiquiditySide> data){
        data.sort(new LiquiditySide.TimeComparator());
        Collections.reverse(data);

        dailyBuySideModel.clear();
        dailySellSideModel.clear();

        for(LiquiditySide e : data){
            if(e.getSide().equalsIgnoreCase("Buy")){
                dailyBuySideModel.addItem(e);
            } else {
                dailySellSideModel.addItem(e);
            }
        }

        dailySellSideModel.fireTableDataChanged();
        dailyBuySideModel.fireTableDataChanged();
        dailySellSideTable.invalidate();
        dailyBuySideTable.invalidate();
    }

    public void addWeekly(List<LiquiditySide> data){
        data.sort(new LiquiditySide.TimeComparator());
        Collections.reverse(data);
    }

    public void clear(){
        dailySellSideModel.clear();
        dailySellSideModel.fireTableDataChanged();
        dailySellSideTable.invalidate();

        dailyBuySideModel.clear();
        dailyBuySideModel.fireTableDataChanged();
        dailyBuySideTable.invalidate();

        pricegapTableModelDaily.clear();
        pricegapTableModelDaily.fireTableDataChanged();
        priceGapTableDaily.invalidate();

        pricegapTableModelIntraDay.clear();
        pricegapTableModelIntraDay.fireTableDataChanged();
        priceGapTableIntraDay.invalidate();
    }

    public void addDailyPriceGaps(List<PriceGap> data) {
        pricegapTableModelDaily.clear();
        pricegapTableModelDaily.addItems(data);
        pricegapTableModelDaily.fireTableDataChanged();
        priceGapTableDaily.invalidate();
    }

    public void addIntraDayPriceGaps(List<PriceGap> data) {
        pricegapTableModelIntraDay.clear();
        pricegapTableModelIntraDay.addItems(data);
        pricegapTableModelIntraDay.fireTableDataChanged();
        priceGapTableIntraDay.invalidate();
    }

}
