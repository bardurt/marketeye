package client.awt.components.views;

import client.awt.components.tabs.VpaTab;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.domain.utils.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class ReportView extends JPanel {

    private Callback callback;
    private static final int DEFAULT_PERCENTILE = 90;

    private JComboBox comboTimeFrame;
    private JComboBox comboDataSize;
    private TextField textFieldSymbol;
    private Checkbox checkboxCache;
    private JRadioButton rbCrypto;
    private JRadioButton rbStock;

    private List<DataSize> dataSizeList = new ArrayList<>();
    private List<TimeInterval> timeIntervalList = new ArrayList<>();

    public ReportView() {
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

        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(new JLabel("Asset"), constraints);

        rbStock = new JRadioButton("STX");
        rbStock.setSelected(true);

        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(rbStock, constraints);

        rbCrypto = new JRadioButton("CRPT");
        rbCrypto.setSelected(false);

        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(rbCrypto, constraints);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbCrypto);
        buttonGroup.add(rbStock);

        JLabel labelTimeFrame = new JLabel("Time Frame");
        constraints.gridx = 3;
        constraints.gridy = 0;
        panel.add(labelTimeFrame, constraints);

        comboTimeFrame = new JComboBox();
        constraints.gridx = 3;
        constraints.gridy = 1;
        panel.add(comboTimeFrame, constraints);

        JLabel labelDataSize = new JLabel("Data Size");

        constraints.gridx = 4;
        constraints.gridy = 0;
        panel.add(labelDataSize, constraints);

        comboDataSize = new JComboBox();
        constraints.gridx = 4;
        constraints.gridy = 1;
        panel.add(comboDataSize, constraints);

        JLabel labelCache = new JLabel("Cache");

        constraints.gridx = 5;
        constraints.gridy = 0;
        panel.add(labelCache, constraints);

        checkboxCache = new Checkbox();
        checkboxCache.setState(true);
        constraints.gridx = 5;
        constraints.gridy = 1;
        panel.add(checkboxCache, constraints);

        JButton buttonCreateReport = new JButton("Create Report");
        buttonCreateReport.setBounds(50, 100, 95, 30);
        buttonCreateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReport();
            }
        });

        constraints.gridx = 6;
        constraints.gridy = 1;
        panel.add(buttonCreateReport, constraints);

        rbCrypto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkboxCache.setState(false);
                    checkboxCache.setVisible(false);
                }
                else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    checkboxCache.setState(true);
                    checkboxCache.setVisible(true);
                }
            }
        });

        add(panel);
    }

    private void createReport() {
        if (callback != null) {
            String symbolText = textFieldSymbol.getText();


            TimeInterval timeInterval = timeIntervalList.get(comboTimeFrame.getSelectedIndex());

            DataSize dataSize = dataSizeList.get(comboDataSize.getSelectedIndex());

            boolean fundamentals = true;// checkBoxFundamentals.getState();

            boolean cache = checkboxCache.getState();

            DataBroker.Asset asset = DataBroker.Asset.Stock;
            if(rbCrypto.isSelected()){
                asset = DataBroker.Asset.Crypto;
                cache = false;
            }

            callback.reportButtonClicked(symbolText, DEFAULT_PERCENTILE, timeInterval, dataSize, fundamentals, cache, asset);

        }
    }

    private void toggleAsset(){

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

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void adjustToProvider(DataProvider dataProvider){

        if(dataProvider == DataProvider.YAHOO_FINANCE){
            checkboxCache.setState(false);
            checkboxCache.setEnabled(false);
        } else {
            checkboxCache.setState(true);
            checkboxCache.setEnabled(true);
        }
    }

    public interface Callback {
        void reportButtonClicked(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize, boolean fundamentals, boolean cache, DataBroker.Asset asset);
    }
}
