package client.awt.components.tabs;

import com.zygne.stockanalyzer.domain.model.enums.TimeFrame;
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

    private JLabel labelApiKeyValue;
    private JComboBox comboTimeFrame;
    private JComboBox comboDataSize;
    private JLabel labelSymbol;
    private JLabel labelStatus;
    private JLabel labelLoading;
    private Checkbox checkBoxFundamentals;
    private TextField textFieldSymbol;
    private TextField textFieldReportPercentile;

    private List<Integer> dataSizeList = new ArrayList<>();
    private List<TimeFrame> timeFrameList = new ArrayList<>();

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
        //panel.add(labelFundamentals, constraints);

        checkBoxFundamentals = new Checkbox();
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
       // panel.add(checkBoxFundamentals, constraints);

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

        JLabel labelApiKey = new JLabel("Api Key");

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(labelApiKey, constraints);

        labelApiKeyValue = new JLabel("");

        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(labelApiKeyValue, constraints);

        add(panel, BorderLayout.NORTH);
    }

    private void createReport() {
        if (callback != null) {
            String symbolText = textFieldSymbol.getText();

            double percentile = 90;

            String percent = textFieldReportPercentile.getText();

            if (StringUtils.idDouble(percent)) {
                percentile = Double.parseDouble(percent);
            }

            TimeFrame timeFrame = timeFrameList.get(comboTimeFrame.getSelectedIndex());

            int dataSize = dataSizeList.get(comboDataSize.getSelectedIndex());

            boolean fundamentals = false;//checkBoxFundamentals.getState();

            callback.generateReport(symbolText, percentile, timeFrame, dataSize, fundamentals);

        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
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

    public void setDataSize(List<Integer> data, int defaultSelection) {
        dataSizeList.clear();
        dataSizeList.addAll(data);
        if (comboDataSize != null) {
            for (Integer e : dataSizeList) {
                comboDataSize.addItem(e + " Month(s)");
            }
            comboDataSize.setSelectedIndex(defaultSelection);
        }
    }

    public void setApiKey(String apiKey){
        labelApiKeyValue.setText(apiKey);
    }

    public interface Callback {
        void generateReport(String symbol, double percentile, TimeFrame timeFrame, int dataSize, boolean fundamentals);
    }
}
