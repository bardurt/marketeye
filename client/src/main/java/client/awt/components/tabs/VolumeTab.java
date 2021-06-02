package client.awt.components.tabs;

import client.awt.components.views.VolumeBarView;
import com.zygne.stockanalyzer.domain.model.VolumeBarDetails;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VolumeTab extends JPanel {

    private JPanel dailyContainer;

    public VolumeTab() {
        setLayout(new BorderLayout());

        JPanel volumePanel = new JPanel(new BorderLayout());
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(0,1));
        volumePanel.add(separator,
                BorderLayout.LINE_START);

        dailyContainer = new JPanel(new GridLayout(0, 4));
        volumePanel.add(dailyContainer, BorderLayout.CENTER);

        add(volumePanel, BorderLayout.CENTER);
    }

    public void addHighVolBars(List<VolumeBarDetails> data) {
        dailyContainer.removeAll();

        int count = 0;
        for (VolumeBarDetails e : data) {
            VolumeBarView v = new VolumeBarView();
            v.populateFrom(e);

            dailyContainer.add(v);
            count++;
            if (count > 10){
                break;
            }
        }
        dailyContainer.repaint();
        repaint();
    }
}