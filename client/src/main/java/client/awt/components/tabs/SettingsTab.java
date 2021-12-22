package client.awt.components.tabs;

import client.awt.ResourceLoader;
import client.awt.components.views.ReportView;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class SettingsTab extends BaseTab implements ReportView.Callback {

    private Callback callback;
    private TextArea textAreaInfo;
    private TextArea textAreaLog;
    private JRadioButton rbAv;
    private JRadioButton rbYahoo;
    private JRadioButton rbCryptoCompare;
    private ReportView reportView;
    private ResourceLoader resourceLoader;

    public SettingsTab(ResourceLoader resourceLoader) {
        super();
        setLayout(new BorderLayout());

        JPanel providerPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        JButton btnLoadSettings = new JButton("Load Settings");
        // providerPanel.add(btnLoadSettings, gridBagConstraints);
        btnLoadSettings.addActionListener(e -> loadSettings());

        rbAv = new JRadioButton("Alpha Vantage");

        rbYahoo = new JRadioButton("Yahoo Finance");

        rbCryptoCompare = new JRadioButton("Crypto Compare");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbAv);
        buttonGroup.add(rbYahoo);
        buttonGroup.add(rbCryptoCompare);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        providerPanel.add(rbAv, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        providerPanel.add(rbYahoo, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
       // providerPanel.add(rbCryptoCompare, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;

        JButton btnSelectProvider = new JButton("Set");
        providerPanel.add(btnSelectProvider, gridBagConstraints);
        btnSelectProvider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectProvider();
            }
        });

        textAreaInfo = new TextArea("");
        textAreaInfo.setEditable(false);

        textAreaLog = new TextArea("");
        textAreaLog.setEditable(false);

        reportView = new ReportView();
        reportView.setCallback(this);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2));
        infoPanel.add(textAreaInfo);
        infoPanel.add(textAreaLog);

        add(reportView, BorderLayout.NORTH);
        add(providerPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

    }

    public void setSettings(Settings settings) {
        textAreaInfo.setText(settings.toString());

        if (settings.getDataProvider() == DataProvider.ALPHA_VANTAGE) {
            rbAv.setSelected(true);
            reportView.adjustToProvider(DataProvider.ALPHA_VANTAGE);
        } else  if (settings.getDataProvider() == DataProvider.CRYPTO_COMPARE) {
            rbCryptoCompare.setSelected(true);
            reportView.adjustToProvider(DataProvider.CRYPTO_COMPARE);
        } else {
            rbYahoo.setSelected(true);
            reportView.adjustToProvider(DataProvider.YAHOO_FINANCE);
        }

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void selectProvider() {

        if (callback == null) {
            return;
        }

        if (rbAv.isSelected()) {
            reportView.adjustToProvider(DataProvider.ALPHA_VANTAGE);
            callback.onProviderSelected(DataProvider.ALPHA_VANTAGE);
        } else if (rbYahoo.isSelected())  {
            reportView.adjustToProvider(DataProvider.YAHOO_FINANCE);
            callback.onProviderSelected(DataProvider.YAHOO_FINANCE);
        } else if (rbCryptoCompare.isSelected())  {
            reportView.adjustToProvider(DataProvider.CRYPTO_COMPARE);
            callback.onProviderSelected(DataProvider.CRYPTO_COMPARE);
        }

    }

    private void loadSettings() {
        JFileChooser jFileChooser = new JFileChooser();

        int returnVal = jFileChooser.showOpenDialog(textAreaInfo);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();

            if (callback != null) {
                System.out.println("Settings Path " + file.getAbsolutePath());
            }
        } else {

        }
    }

    public void setTimeFrames(java.util.List<TimeInterval> data, int defaultSelection) {
        reportView.setTimeFrames(data, defaultSelection);
    }

    public void setDataSize(List<DataSize> data, int defaultSelection) {
        reportView.setDataSize(data, defaultSelection);
    }

    public interface Callback {
        void onProviderSelected(DataProvider dataProvider);

        void generateReport(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize, boolean fundamentals, boolean cache, DataBroker.Asset asset);
        void fetchLastPrice(String symbol);
    }

    @Override
    public void reportButtonClicked(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize, boolean fundamentals, boolean cache, DataBroker.Asset asset) {
        if (callback != null) {
            callback.generateReport(symbol, percentile, timeInterval, dataSize, fundamentals, cache, asset);
        }
    }

    public TextArea getLogArea() {
        return textAreaLog;
    }

}