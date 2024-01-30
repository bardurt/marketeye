package client.awt.components.tabs;

import client.awt.components.views.StocksView;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SettingsTab extends JPanel implements StocksView.Callback {

    private final Callback callback;
    private final StocksView reportView;

    public SettingsTab(Callback callback) {
        super();
        this.callback = callback;
        setLayout(new BorderLayout());

        JPanel providerPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        TextArea textAreaInfo = new TextArea("");
        textAreaInfo.setEditable(false);

        TextArea textAreaLog = new TextArea("");
        textAreaLog.setEditable(false);

        reportView = new StocksView();
        reportView.setCallback(this);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2));
        infoPanel.add(textAreaInfo);
        infoPanel.add(textAreaLog);

        add(reportView, BorderLayout.NORTH);
        add(providerPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

    }

    public void setTimeFrames(java.util.List<TimeInterval> data, int defaultSelection) {
        reportView.setTimeFrames(data, defaultSelection);
    }

    public void setDataSize(List<DataSize> data, int defaultSelection) {
        reportView.setDataSize(data, defaultSelection);
    }

    public interface Callback {
        void generateReport(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize);
    }

    @Override
    public void reportButtonClicked(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize) {
        if (callback != null) {
            callback.generateReport(symbol, percentile, timeInterval, dataSize);
        }
    }

}