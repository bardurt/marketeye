package client.awt.components.views;

import com.zygne.stockanalyzer.domain.model.Fundamentals;

import javax.swing.*;
import java.awt.*;

public class FundamentalsView extends JPanel {


    private JTextField textFieldFloat;
    private JTextField textFieldPriceHigh;
    private JTextField textFieldPriceLow;
    private JTextField textFieldAvgVol;

    public FundamentalsView() {
        setLayout(new GridLayout());

        JPanel paneFloat = new JPanel(new BorderLayout());

        paneFloat.add(new JLabel("Stock Float"), BorderLayout.NORTH);

        textFieldFloat = new JTextField("-");
        paneFloat.add(textFieldFloat, BorderLayout.CENTER);

        add(paneFloat);

        JPanel panelHigh = new JPanel(new BorderLayout());

        panelHigh.add(new JLabel("52 week high"), BorderLayout.NORTH);

        textFieldPriceHigh = new JTextField("-");
        panelHigh.add(textFieldPriceHigh, BorderLayout.CENTER);

        add(panelHigh);

        JPanel panelLow = new JPanel(new BorderLayout());

        panelLow.add(new JLabel("52 week low"), BorderLayout.NORTH);

        textFieldPriceLow = new JTextField("-");
        panelLow.add(textFieldPriceLow, BorderLayout.CENTER);

        add(panelLow);

        JPanel panelAvgVol = new JPanel(new BorderLayout());

        panelAvgVol.add(new JLabel("Avg Bar Vol"), BorderLayout.NORTH);

        textFieldAvgVol = new JTextField("-");
        panelAvgVol.add(textFieldAvgVol, BorderLayout.CENTER);

        add(panelAvgVol);

    }

    public void populateFrom(Fundamentals fundamentals){

        textFieldFloat.setText(String.format("%,d", fundamentals.getSharesFloat()));
        textFieldPriceHigh.setText(String.format("%.2f", fundamentals.getHigh()));
        textFieldPriceLow.setText(String.format("%.2f", fundamentals.getLow()));
        textFieldAvgVol.setText(String.format("%,d", fundamentals.getAvgVol()));
    }

    public void clear(){
        textFieldFloat.setText("");
        textFieldPriceHigh.setText("");
        textFieldPriceLow.setText("");
        textFieldAvgVol.setText("");
    }
}
