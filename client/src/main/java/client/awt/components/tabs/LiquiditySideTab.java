package client.awt.components.tabs;

import client.awt.components.tables.*;
import com.zygne.stockanalyzer.domain.model.GapHistory;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.PriceGap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class LiquiditySideTab extends BaseTab {

    private Callback callback;
    private LiquiditySideTableModel dailySellSideModel;
    private JTable dailySellSideTable;

    private LiquiditySideTableModel dailyBuySideModel;
    private JTable dailyBuySideTable;

    private JTable priceGapTableDaily;
    private PriceGapTableModel pricegapTableModelDaily;

    private JTable gapHistoryTable;
    private GapHistoryTableModel gapHistoryTableModel;


    private JButton startButton;


    public LiquiditySideTab() {
        super();
        setLayout(new BorderLayout());

        startButton = new JButton("Start");
        add(startButton, BorderLayout.NORTH);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findLiquidity();
            }
        });

        JPanel contentPanel = new ContentPanel();
        contentPanel.setLayout(new GridLayout(2,0));

        dailySellSideModel = new LiquiditySideTableModel();
        dailySellSideTable = new JTable(dailySellSideModel);
        dailySellSideTable.setDefaultRenderer(String.class, new LiquiditySideRenderer());
        dailySellSideTable.setRowSorter(dailySellSideModel.getSorter(dailySellSideTable.getModel()));

        dailyBuySideModel = new LiquiditySideTableModel();
        dailyBuySideTable = new JTable(dailyBuySideModel);
        dailyBuySideTable.setDefaultRenderer(String.class, new LiquiditySideRenderer());
        dailyBuySideTable.setRowSorter(dailyBuySideModel.getSorter(dailyBuySideTable.getModel()));

        JPanel gapHistoryPanel = new ContentPanel();
        gapHistoryPanel.setLayout(new BorderLayout());

        gapHistoryPanel.add(new JLabel("Gap Statistic"), BorderLayout.NORTH);
        gapHistoryPanel.add(new JScrollPane(gapHistoryTable), BorderLayout.CENTER);

        pricegapTableModelDaily = new PriceGapTableModel();

        priceGapTableDaily = new JTable(pricegapTableModelDaily);
        priceGapTableDaily.setDefaultRenderer(String.class, new PriceGapRenderer());;

        JPanel dailyPanel = new ContentPanel();
        dailyPanel.setLayout(new BorderLayout());

        JPanel dailyContainer = new ContentPanel();
        dailyContainer.setLayout(new GridLayout(0,2));

        JPanel dailyBuy = new JPanel(new BorderLayout());
        dailyBuy.add(new JLabel("Buy Side"), BorderLayout.NORTH);
        dailyBuy.add(new JScrollPane(dailyBuySideTable), BorderLayout.CENTER);

        dailyContainer.add(dailyBuy);

        JPanel dailySell = new JPanel(new BorderLayout());
        dailySell.add(new JLabel("Sell Side"), BorderLayout.NORTH);
        dailySell.add(new JScrollPane(dailySellSideTable), BorderLayout.CENTER);

        dailyContainer.add(dailySell);
        dailyPanel.add(dailyContainer);

        contentPanel.add(dailyPanel);

        JPanel priceGapsPanel = new ContentPanel();
        priceGapsPanel.setLayout(new GridLayout());

        JPanel priceGapsDailyPanel = new ContentPanel();
        priceGapsDailyPanel.setLayout(new BorderLayout());
        priceGapsDailyPanel.add(new JLabel("Price Gaps Daily"), BorderLayout.NORTH);
        priceGapsDailyPanel.add(new JScrollPane(priceGapTableDaily), BorderLayout.CENTER);

        priceGapsPanel.add(priceGapsDailyPanel);
        contentPanel.add(priceGapsPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void addDaily(List<LiquiditySide> data){
        data.sort(new LiquiditySide.TimeComparator());
        Collections.reverse(data);

        dailyBuySideModel.clear();
        dailySellSideModel.clear();

        for(LiquiditySide e : data){
            if(e.getType() == LiquiditySide.BUY){
                System.out.println("LS " + e.getStart() + " " + e.getSide() );
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
    }

    public void addDailyPriceGaps(List<PriceGap> data) {
        pricegapTableModelDaily.clear();
        pricegapTableModelDaily.addItems(data);
        pricegapTableModelDaily.fireTableDataChanged();
        priceGapTableDaily.invalidate();
    }

    public void addGapStatistic(GapHistory gapHistory){
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void findLiquidity(){
        if(callback != null){
            callback.findLiquidity();
        }
    }

    public interface Callback{
        void findLiquidity();
    }

}
