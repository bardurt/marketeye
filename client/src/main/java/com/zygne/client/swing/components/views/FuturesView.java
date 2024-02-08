package com.zygne.client.swing.components.views;

import com.zygne.data.domain.model.Asset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class FuturesView extends JPanel {

    private Callback callback;

    private JComboBox comboAsset;
    private JCheckBox dualChart;
    private JRadioButton leftChart;
    private JRadioButton rightChart;

    private java.util.List<Asset> assetList = new ArrayList<>();

    public FuturesView() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel optionsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        JLabel labelSymbol = new JLabel("Symbol");

        constraints.gridx = 0;
        constraints.gridy = 0;
        optionsPanel.add(labelSymbol, constraints);

        comboAsset = new JComboBox();
        constraints.gridx = 0;
        constraints.gridy = 1;
        optionsPanel.add(comboAsset, constraints);


        JLabel labelDual = new JLabel("Dual Chart");

        constraints.gridx = 1;
        constraints.gridy = 0;
        optionsPanel.add(labelDual, constraints);

        dualChart = new JCheckBox();
        constraints.gridx = 1;
        constraints.gridy = 1;
        optionsPanel.add(dualChart, constraints);
        dualChart.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 2) {
                    callback.toggleDualChart(false);
                } else {
                    callback.toggleDualChart(true);
                }
            }
        });

        JLabel labelLeft = new JLabel("Left Chart");

        constraints.gridx = 0;
        constraints.gridy = 2;
        optionsPanel.add(labelLeft, constraints);

        leftChart = new JRadioButton();
        constraints.gridx = 0;
        constraints.gridy = 3;
        optionsPanel.add(leftChart, constraints);

        leftChart.setSelected(true);
        JLabel rightLabel = new JLabel("Right Chart");

        constraints.gridx = 1;
        constraints.gridy = 2;
        optionsPanel.add(rightLabel, constraints);

        rightChart = new JRadioButton();
        constraints.gridx = 1;
        constraints.gridy = 3;
        optionsPanel.add(rightChart, constraints);

        leftChart.addActionListener(e -> {
            boolean selected = ((JRadioButton) e.getSource()).isSelected();
            if (selected) {
                callback.onChartSelected(0);
            }
        });

        rightChart.addActionListener(e -> {
            boolean selected = ((JRadioButton) e.getSource()).isSelected();
            if (selected) {
                callback.onChartSelected(1);
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(leftChart);
        group.add(rightChart);


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
            String name = assetList.get(comboAsset.getSelectedIndex()).getDisplayName();
            String symbolText = assetList.get(comboAsset.getSelectedIndex()).getBrokerName();
            callback.reportButtonClicked(name, symbolText);
        }
    }


    public void setAssets(List<Asset> data) {
        assetList.clear();
        assetList.addAll(data);
        comboAsset.removeAllItems();
        if (comboAsset != null) {
            for (Asset e : assetList) {
                comboAsset.addItem(e.getDisplayName());
            }
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void reportButtonClicked(String name, String symbol);

        void toggleDualChart(boolean active);

        void onChartSelected(int chart);
    }
}