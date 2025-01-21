package com.zygne.client.swing.components.views;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;

public class SettingsView extends JPanel {

    private Callback callback;
    private TextField textFieldSymbol;

    public SettingsView() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel optionsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        JLabel labelSymbol = new JLabel("Symbol");

        textFieldSymbol = new TextField();
        textFieldSymbol.setColumns(18);

        constraints.gridx = 0;
        constraints.gridy = 0;
        optionsPanel.add(labelSymbol, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        optionsPanel.add(textFieldSymbol, constraints);


        JPanel buttonPanel = new JPanel(new GridLayout(1,2));

        JButton buttonEquities = new JButton("Equity");
        buttonEquities.setBounds(50, 100, 95, 30);
        buttonEquities.addActionListener(e -> {
            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 0);
        });

        buttonPanel.add(buttonEquities);

        JButton buttonCrypto = new JButton("Crypto");
        buttonCrypto.setBounds(50, 100, 95, 30);
        buttonCrypto.addActionListener(e -> {

            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 1);

        });

        buttonPanel.add(buttonCrypto);

        constraints.gridx = 0;
        constraints.gridy = 2;
        optionsPanel.add(buttonPanel, constraints);

        mainPanel.add(optionsPanel, BorderLayout.NORTH);
        add(mainPanel);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void reportButtonClicked(String symbol, int type);
    }
}
