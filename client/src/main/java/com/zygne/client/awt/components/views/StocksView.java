package com.zygne.client.awt.components.views;

import com.zygne.data.domain.model.DataSize;
import com.zygne.data.domain.model.enums.TimeInterval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StocksView extends JPanel {

    private Callback callback;
    private static final int DEFAULT_PERCENTILE = 90;

    private JComboBox comboTimeFrame;
    private JComboBox comboDataSize;
    private TextField textFieldSymbol;

    private List<DataSize> dataSizeList = new ArrayList<>();
    private List<TimeInterval> timeIntervalList = new ArrayList<>();

    public StocksView() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel optionsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        JLabel labelSymbol = new JLabel("Symbol");

        textFieldSymbol = new TextField();
        textFieldSymbol.setColumns(12);

        constraints.gridx = 0;
        constraints.gridy = 0;
        optionsPanel.add(labelSymbol, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        optionsPanel.add(textFieldSymbol, constraints);


        JLabel labelTimeFrame = new JLabel("Time Frame ");
        constraints.gridx = 3;
        constraints.gridy = 0;
        optionsPanel.add(labelTimeFrame, constraints);

        comboTimeFrame = new JComboBox();
        constraints.gridx = 3;
        constraints.gridy = 1;
        optionsPanel.add(comboTimeFrame, constraints);

        JLabel labelDataSize = new JLabel("Data Size");

        constraints.gridx = 4;
        constraints.gridy = 0;
        optionsPanel.add(labelDataSize, constraints);

        comboDataSize = new JComboBox();
        constraints.gridx = 4;
        constraints.gridy = 1;
        optionsPanel.add(comboDataSize, constraints);

        mainPanel.add(optionsPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JButton buttonCreateReport = new JButton("Create Report");
        buttonCreateReport.setBounds(50, 100, 95, 30);
        buttonCreateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReport();
            }
        });

        buttonPanel.add(buttonCreateReport, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void createReport() {
        if (callback != null) {
            String symbolText = textFieldSymbol.getText();

            TimeInterval timeInterval = timeIntervalList.get(comboTimeFrame.getSelectedIndex());

            DataSize dataSize = dataSizeList.get(comboDataSize.getSelectedIndex());

            callback.reportButtonClicked(symbolText, DEFAULT_PERCENTILE, timeInterval, dataSize);

        }
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

    public interface Callback {
        void reportButtonClicked(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize);
    }
}
