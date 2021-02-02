package client.awt.components.tabs;

import client.awt.components.tables.LiquidityLevelRenderer;
import client.awt.components.tables.LiquidityLevelTableModel;
import client.awt.components.views.FundamentalsView;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.domain.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VpaTab extends JPanel {

    private static final int DEFAULT_PERCENTILE = 90;

    private Callback callback;

    private JComboBox comboTimeFrame;
    private JComboBox comboDataSize;
    private Checkbox checkBoxFundamentals;
    private TextField textFieldSymbol;
    private TextField textFieldReportPercentile;

    private List<DataSize> dataSizeList = new ArrayList<>();
    private List<TimeInterval> timeIntervalList = new ArrayList<>();

    private LiquidityLevelTableModel tableModelPrices;
    private LiquidityLevelTableModel tableModelVolume;

    private JTable tablePrices;
    private JTable tableVolume;

    private FundamentalsView fundamentalsView;

    public VpaTab() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        JLabel labelSymbol = new JLabel("Symbol");

        textFieldSymbol = new TextField();
        textFieldSymbol.setColumns(12);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(labelSymbol, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(textFieldSymbol, constraints);

        JLabel labelPercentile = new JLabel("Percentile");
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(labelPercentile, constraints);

        textFieldReportPercentile = new TextField();
        textFieldReportPercentile.setColumns(12);
        textFieldReportPercentile.setText("" + DEFAULT_PERCENTILE);

        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(textFieldReportPercentile, constraints);

        JLabel labelTimeFrame = new JLabel("Time Frame");
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(labelTimeFrame, constraints);

        comboTimeFrame = new JComboBox();
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(comboTimeFrame, constraints);

        JLabel labelDataSize = new JLabel("Data Size");

        constraints.gridx = 3;
        constraints.gridy = 0;
        panel.add(labelDataSize, constraints);

        comboDataSize = new JComboBox();
        constraints.gridx = 3;
        constraints.gridy = 1;
        panel.add(comboDataSize, constraints);

        JLabel labelFundamentals = new JLabel("Fundamentals");
        constraints.gridx = 4;
        constraints.gridy = 0;
        panel.add(labelFundamentals, constraints);

        checkBoxFundamentals = new Checkbox();
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(checkBoxFundamentals, constraints);

        JButton buttonCreateReport = new JButton("Create Report");
        buttonCreateReport.setBounds(50, 100, 95, 30);
        buttonCreateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReport();
            }
        });

        constraints.gridx = 5;
        constraints.gridy = 1;
        panel.add(buttonCreateReport, constraints);

        add(panel, BorderLayout.NORTH);

        JPanel contentTable = new JPanel(new BorderLayout());

        fundamentalsView = new FundamentalsView();

        contentTable.add(fundamentalsView, BorderLayout.NORTH);

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

        contentTable.add(tablesPanel, BorderLayout.CENTER);

        add(contentTable, BorderLayout.CENTER);
    }

    private void createReport() {
        if (callback != null) {
            String symbolText = textFieldSymbol.getText();

            double percentile = 90;

            String percent = textFieldReportPercentile.getText();

            if (StringUtils.idDouble(percent)) {
                percentile = Double.parseDouble(percent);
            }

            TimeInterval timeInterval = timeIntervalList.get(comboTimeFrame.getSelectedIndex());

            int dataSize = dataSizeList.get(comboDataSize.getSelectedIndex()).getSize();

            boolean fundamentals = checkBoxFundamentals.getState();

            callback.generateReport(symbolText, percentile, timeInterval, dataSize, fundamentals);

        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setTimeFrames(List<TimeInterval> data, int defaultSelection) {
        timeIntervalList.clear();
        timeIntervalList.addAll(data);
        comboTimeFrame.removeAllItems();
        if (comboTimeFrame != null) {
            for (TimeInterval e : timeIntervalList) {
                comboTimeFrame.addItem(e.toString());
            }
            comboTimeFrame.setSelectedIndex(defaultSelection);
        }
    }

    public void setDataSize(List<DataSize> data, int defaultSelection) {
        dataSizeList.clear();
        dataSizeList.addAll(data);
        comboDataSize.removeAllItems();
        if (comboDataSize != null) {
            for (DataSize e : dataSizeList) {
                comboDataSize.addItem(e.getSize() + " " + e.getUnit());
            }
            comboDataSize.setSelectedIndex(defaultSelection);
        }
    }

    public void addResistance(List<LiquidityLevel> data){
        fundamentalsView.clear();
        if (tablePrices != null) {
            tableModelPrices.clear();
            data.sort(new LiquidityLevel.PriceComparator());
            Collections.reverse(data);
            tableModelPrices.addItems(data);
            tableModelPrices.fireTableDataChanged();
            tablePrices.invalidate();
        }

        if (tableModelVolume != null) {
            tableModelVolume.clear();
            data.sort(new LiquidityLevel.VolumeComparator());
            Collections.reverse(data);
            tableModelVolume.addItems(data);
            tableModelVolume.fireTableDataChanged();
            tableVolume.invalidate();
        }
    }

    public void addFundamentals(Fundamentals fundamentals){
        fundamentalsView.populateFrom(fundamentals);
    }

    public void reset(){
        fundamentalsView.clear();
    }

    public interface Callback {
        void generateReport(String symbol, double percentile, TimeInterval timeInterval, int dataSize, boolean fundamentals);
    }
}
