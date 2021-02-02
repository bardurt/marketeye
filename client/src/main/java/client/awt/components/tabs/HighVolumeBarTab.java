package client.awt.components.tabs;

import client.awt.components.views.VolumeBarView;
import com.zygne.stockanalyzer.domain.model.VolumeBarDetails;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighVolumeBarTab extends JPanel {

    private JPanel dailyContainer;

    public HighVolumeBarTab() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        dailyContainer = new JPanel(new GridLayout(0, 4));

        dailyContainer.add(new VolumeBarView());
        dailyContainer.add(new VolumeBarView());
        dailyContainer.add(new VolumeBarView());
        dailyContainer.add(new VolumeBarView());

        add(dailyContainer);
    }

    public void addItems(List<VolumeBarDetails> data) {
        dailyContainer.removeAll();

        for (VolumeBarDetails e : data) {
            VolumeBarView v = new VolumeBarView();
            v.populateFrom(e);

            dailyContainer.add(v);
        }

        repaint();
    }
}
