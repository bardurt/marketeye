package client.awt.components.tabs;

import com.zygne.stockanalyzer.domain.model.DataLength;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.domain.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainTab extends JPanel {

    private static final int DEFAULT_PERCENTILE = 90;

    private Callback callback;

    private TextArea textAreaInfo;
    private JComboBox comboTimeFrame;
    private JComboBox comboDataSize;
    private Checkbox checkBoxFundamentals;
    private TextField textFieldSymbol;
    private TextField textFieldReportPercentile;

    private List<DataLength> dataSizeList = new ArrayList<>();
    private List<TimeInterval> timeIntervalList = new ArrayList<>();

    public MainTab() {
        setLayout(new BorderLayout());

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

        JLabel labelPercentile = new JLabel("Percentile");
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(labelPercentile, constraints);

        textFieldReportPercentile = new TextField();
        textFieldReportPercentile.setColumns(12);
        textFieldReportPercentile.setText("" + DEFAULT_PERCENTILE);

        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(textFieldReportPercentile, constraints);

        JLabel labelTimeFrame = new JLabel("Time Frame");
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(labelTimeFrame, constraints);

        comboTimeFrame = new JComboBox();
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(comboTimeFrame, constraints);

        JLabel labelDataSize = new JLabel("Data Size");
        ;
        constraints.gridx = 3;
        constraints.gridy = 0;
        panel.add(labelDataSize, constraints);

        comboDataSize = new JComboBox();
        constraints.gridx = 3;
        constraints.gridy = 1;
        panel.add(comboDataSize, constraints);

        JLabel labelFundamentals = new JLabel("Fundamentals");
        constraints.gridx = 4;
        constraints.gridy = 0;
        panel.add(labelFundamentals, constraints);

        checkBoxFundamentals = new Checkbox();
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(checkBoxFundamentals, constraints);

        JButton buttonCreateReport = new JButton("Create Report");
        buttonCreateReport.setBounds(50, 100, 95, 30);
        buttonCreateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReport();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(buttonCreateReport, constraints);

        JPanel infoPanel = new JPanel(new BorderLayout());

        textAreaInfo = new TextArea("");
        textAreaInfo.setEditable(false);

        infoPanel.add(textAreaInfo);

        add(panel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void createReport() {
        if (callback != null) {
            String symbolText = textFieldSymbol.getText();

            double percentile = 90;

            String percent = textFieldReportPercentile.getText();

            if (StringUtils.idDouble(percent)) {
                percentile = Double.parseDouble(percent);
            }

            TimeInterval timeInterval = timeIntervalList.get(comboTimeFrame.getSelectedIndex());

            int dataSize = dataSizeList.get(comboDataSize.getSelectedIndex()).getSize();

            boolean fundamentals = checkBoxFundamentals.getState();

            callback.generateReport(symbolText, percentile, timeInterval, dataSize, fundamentals);

        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setTimeFrames(List<TimeInterval> data, int defaultSelection) {
        timeIntervalList.clear();
        timeIntervalList.addAll(data);
        if (comboTimeFrame != null) {
            for (TimeInterval e : timeIntervalList) {
                comboTimeFrame.addItem(e.toString());
            }
            comboTimeFrame.setSelectedIndex(defaultSelection);
        }
    }

    public void setDataSize(List<DataLength> data, int defaultSelection) {
        dataSizeList.clear();
        dataSizeList.addAll(data);
        if (comboDataSize != null) {
            for (DataLength e : dataSizeList) {
                comboDataSize.addItem(e.getSize() + " " + e.getContext());
            }
            comboDataSize.setSelectedIndex(defaultSelection);
        }
    }

    public void setSettings(Settings settngs){
        textAreaInfo.setText(settngs.toString());
    }

    public interface Callback {
        void generateReport(String symbol, double percentile, TimeInterval timeInterval, int dataSize, boolean fundamentals);
    }
}
