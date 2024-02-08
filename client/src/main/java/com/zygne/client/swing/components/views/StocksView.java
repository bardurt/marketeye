package com.zygne.client.swing.components.views;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StocksView extends JPanel {

    private Callback callback;
    private TextField textFieldSymbol;

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

        JButton buttonCreateReport = new JButton("Create Report");
        buttonCreateReport.setBounds(50, 100, 95, 30);
        buttonCreateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReport();
            }
        });

        constraints.gridx = 1;
        constraints.gridy = 1;
        optionsPanel.add(buttonCreateReport, constraints);

        mainPanel.add(optionsPanel, BorderLayout.NORTH);
        add(mainPanel);
    }

    private void createReport() {
        if (callback != null) {
            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText);

        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void reportButtonClicked(String symbol);
    }
}
