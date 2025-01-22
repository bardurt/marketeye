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

        JPanel optionsPanel = new JPanel(new GridLayout(2,2));

        textFieldSymbol = new TextField();
        textFieldSymbol.setColumns(18);

        optionsPanel.add(textFieldSymbol);

        JPanel buttonPanel = new JPanel(new GridLayout(1,5));

        JButton buttonAlphaVantage = new JButton("AlphaVantage");
        buttonAlphaVantage.setBounds(50, 100, 95, 30);
        buttonAlphaVantage.addActionListener(e -> {
            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 0);
        });

        buttonPanel.add(buttonAlphaVantage);

        JButton buttonPolygon = new JButton("Polygon");
        buttonPolygon.setBounds(50, 100, 95, 30);
        buttonPolygon.addActionListener(e -> {
            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 1);
        });

        buttonPanel.add(buttonPolygon);

        JButton buttonCrypto = new JButton("Crypto");
        buttonCrypto.setBounds(50, 100, 95, 30);
        buttonCrypto.addActionListener(e -> {

            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 2);

        });

        buttonPanel.add(buttonCrypto);


        JPanel configPanel = new JPanel(new GridLayout(1,2));
        JButton buttonAdjust = new JButton("Adjust");
        buttonAdjust.setBounds(50, 100, 95, 30);
        buttonAdjust.addActionListener(e -> {
            callback.configButtonClicked(true);
        });

        configPanel.add(buttonAdjust);

        JButton buttonRaw = new JButton("Raw");
        buttonRaw.setBounds(50, 100, 95, 30);
        buttonRaw.addActionListener(e -> {
            callback.configButtonClicked(false);
        });

        configPanel.add(buttonRaw);

        optionsPanel.add(configPanel);
        optionsPanel.add(buttonPanel);
        mainPanel.add(optionsPanel, BorderLayout.NORTH);
        add(mainPanel);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void reportButtonClicked(String symbol, int type);
        void configButtonClicked(Boolean adjust);
    }
}
