package client.awt.components.views;

import client.awt.components.tables.LiquidityLevelRenderer;
import client.awt.components.tables.LiquidityLevelTableModel;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.VolumeBarDetails;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import javax.swing.*;
import java.util.Collections;

public class VolumeBarView extends JPanel {

    private JLabel labelHigh;
    private JLabel labelLow;
    private JLabel labelDate;
    private JLabel labelVolume;
    private JLabel labelRank;

    private LiquidityLevelTableModel tableModelPrices;
    private JTable tablePrices;


    public VolumeBarView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        labelDate = new JLabel("Date :");
        labelHigh = new JLabel("High :");
        labelLow = new JLabel("Low :");
        labelVolume = new JLabel("Vol :");
        labelRank = new JLabel("Rank :");

        add(labelDate);
        add(labelHigh);
        add(labelLow);
        add(labelVolume);
        add(labelRank);

        tableModelPrices = new LiquidityLevelTableModel();
        tablePrices = new JTable(tableModelPrices);
        tablePrices.setDefaultRenderer(LiquidityLevel.class, new LiquidityLevelRenderer());
        tablePrices.setDefaultRenderer(String.class, new LiquidityLevelRenderer());

        add(new JScrollPane(tablePrices));
    }

    public void populateFrom(VolumeBarDetails volumeBarDetails) {

        labelDate.setText("Date : " + TimeHelper.getDateFromTimeStamp(volumeBarDetails.getTimeStamp()));
        labelHigh.setText("High : " + String.format("%.2f",volumeBarDetails.getHigh()));
        labelLow.setText("Low : " + String.format("%.2f",volumeBarDetails.getLow()));
        labelVolume.setText("Vol : " + String.format("%,d",volumeBarDetails.getVolume()));
        labelRank.setText("Rank : " + volumeBarDetails.getRank());

        if(volumeBarDetails.getSupply() != null) {
            if (tablePrices != null) {
                tableModelPrices.clear();
                volumeBarDetails.getSupply().sort(new LiquidityLevel.PriceComparator());
                Collections.reverse(volumeBarDetails.getSupply());
                tableModelPrices.addItems(volumeBarDetails.getSupply());
                tableModelPrices.fireTableDataChanged();
                tablePrices.invalidate();
            }
        } else {
            tablePrices.setVisible(false);
        }

    }
}
