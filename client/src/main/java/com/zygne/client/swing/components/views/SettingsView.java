package com.zygne.client.swing.components.views;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.FlowLayout;

public class SettingsView extends JPanel {

    private Callback callback;
    private TextField textFieldSymbol;

    public SettingsView(Callback callback) {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel optionsPanel = new JPanel(new GridLayout(2, 2));

        textFieldSymbol = new TextField();
        textFieldSymbol.setColumns(18);
        optionsPanel.add(textFieldSymbol);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));

        JButton buttonAlphaVantage = new JButton("AlphaVantage");
        buttonAlphaVantage.addActionListener(e -> {
            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 0);
        });
        buttonPanel.add(buttonAlphaVantage);

        JButton buttonPolygon = new JButton("Polygon");
        buttonPolygon.addActionListener(e -> {
            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 1);
        });
        buttonPanel.add(buttonPolygon);

        JButton buttonCrypto = new JButton("Crypto");
        buttonCrypto.addActionListener(e -> {
            String symbolText = textFieldSymbol.getText();
            callback.reportButtonClicked(symbolText, 2);
        });
        buttonPanel.add(buttonCrypto);

        optionsPanel.add(buttonPanel);

        JPanel configPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton buttonAdjust = new JButton("Adjust");
        buttonAdjust.addActionListener(e -> {
            callback.configButtonClicked(true);
        });

        JButton buttonRaw = new JButton("Raw");
        buttonRaw.addActionListener(e -> {
            callback.configButtonClicked(false);
        });

        configPanel.add(buttonAdjust);
        configPanel.add(buttonRaw);
        configPanel.setBorder(new EmptyBorder(0, 50, 0, 0));  // 10px space on the left

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        mainPanel.add(configPanel, BorderLayout.EAST);

        add(mainPanel);
    }


    public interface Callback {
        void reportButtonClicked(String symbol, int type);
        void configButtonClicked(Boolean adjust);
    }
}
