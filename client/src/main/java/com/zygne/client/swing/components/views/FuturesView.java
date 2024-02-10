package com.zygne.client.swing.components.views;

import com.zygne.data.domain.model.Asset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FuturesView extends JPanel {

    private Callback callback;

    private final JComboBox comboAsset;

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
            String name = assetList.get(comboAsset.getSelectedIndex()).displayName();
            String symbolText = assetList.get(comboAsset.getSelectedIndex()).brokerName();
            callback.reportButtonClicked(name, symbolText);
        }
    }


    public void setAssets(List<Asset> data) {
        assetList.clear();
        assetList.addAll(data);
        comboAsset.removeAllItems();
        for (Asset e : assetList) {
            comboAsset.addItem(e.displayName());
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void reportButtonClicked(String name, String symbol);
    }
}