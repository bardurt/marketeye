package client.awt.components.tabs;

import client.awt.components.tables.LiquiditySideRenderer;
import client.awt.components.tables.LiquiditySideTableModel;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.enums.TimeFrame;
import com.zygne.stockanalyzer.domain.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LiquiditySideTab extends JPanel {

    private static final int DEFAULT_PERCENTILE = 90;

    private Callback callback;
    private LiquiditySideTableModel liquiditySideTableModel;
    private JComboBox comboTimeFrame;
    private TextField textFieldPrice;
    private TextField textFieldPercentile;
    private JSlider sliderWickSize;
    private JTable table;

    private List<TimeFrame> timeFrameList = new ArrayList<>();

    public LiquiditySideTab() {

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        JLabel labelPercentile = new JLabel("Percentile");
        constraints.gridx = 0;
        constraints.gridy = 0;
        controlPanel.add(labelPercentile, constraints);

        textFieldPercentile = new TextField();
        textFieldPercentile.setColumns(12);
        textFieldPercentile.setText("" + DEFAULT_PERCENTILE);

        constraints.gridx = 0;
        constraints.gridy = 1;
        controlPanel.add(textFieldPercentile, constraints);

        JLabel labelSymbol = new JLabel("Price");

        textFieldPrice = new TextField();
        textFieldPrice.setColumns(12);

        constraints.gridx = 1;
        constraints.gridy = 0;
        controlPanel.add(labelSymbol, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        controlPanel.add(textFieldPrice, constraints);

        JLabel labelTimeFrame = new JLabel("Time Frame");
        constraints.gridx = 2;
        constraints.gridy = 0;
        controlPanel.add(labelTimeFrame, constraints);

        comboTimeFrame = new JComboBox();
        constraints.gridx = 2;
        constraints.gridy = 1;
        controlPanel.add(comboTimeFrame, constraints);

        JLabel labelWickSize = new JLabel("Min Wick Size (%)");
        constraints.gridx = 3;
        constraints.gridy = 0;
        controlPanel.add(labelWickSize, constraints);

        sliderWickSize = new JSlider(5, 55, 15);
        sliderWickSize.setPaintTrack(true);
        sliderWickSize.setPaintTicks(true);
        sliderWickSize.setPaintLabels(true);
        sliderWickSize.setMajorTickSpacing(10);
        sliderWickSize.setMinorTickSpacing(5);

        constraints.gridx = 3;
        constraints.gridy = 1;
        controlPanel.add(sliderWickSize, constraints);

        JButton buttonCreateReport = new JButton("Find");
        buttonCreateReport.setBounds(50, 100, 95, 30);
        buttonCreateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findSides();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        controlPanel.add(buttonCreateReport, constraints);
        add(controlPanel, BorderLayout.NORTH);

        liquiditySideTableModel = new LiquiditySideTableModel();

        table = new JTable(liquiditySideTableModel);
        table.setDefaultRenderer(String.class, new LiquiditySideRenderer());
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 5;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setTimeFrames(List<TimeFrame> data, int defaultSelection) {
        timeFrameList.clear();
        timeFrameList.addAll(data);
        if (comboTimeFrame != null) {
            for (TimeFrame e : timeFrameList) {
                comboTimeFrame.addItem(e);
            }
            comboTimeFrame.setSelectedIndex(defaultSelection);
        }
    }

    public void addSides(List<LiquiditySide> data){
        liquiditySideTableModel.clear();
        liquiditySideTableModel.addItems(data);
        liquiditySideTableModel.fireTableDataChanged();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void findSides() {

        if(callback != null){

            double percentile = 90;


            if (StringUtils.idDouble(textFieldPercentile.getText())) {
                percentile = Double.parseDouble(textFieldPercentile.getText());
            }

            double price = -1;

            if (StringUtils.idDouble(textFieldPrice.getText())) {
                price = Double.parseDouble(textFieldPrice.getText());
            }

            double size = sliderWickSize.getValue();

            TimeFrame timeFrame = timeFrameList.get(comboTimeFrame.getSelectedIndex());

            callback.findLiquiditySides(timeFrame, size, percentile, price);
        }
    }

    public interface Callback{
        void findLiquiditySides(TimeFrame timeFrame, double size, double percentile, double price);
    }
}
