package client.awt.components.tabs;

import com.zygne.zchart.chart.charts.volumeatprice.VolumePricePanel;
import com.zygne.zchart.chart.charts.volumeprofile.VolumeProfilePanel;
import com.zygne.zchart.chart.model.data.Quote;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VpaTab extends JPanel{


    private final VolumeProfilePanel volumeProfilePanel;
    private final VolumePricePanel volumePricePanel;

    public VpaTab() {
        super();
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel tablesPanel = new JPanel(new BorderLayout());

        JPanel volumePanel = new JPanel(new GridLayout(0,2));

        JPanel volumePriceContainer = new JPanel(new BorderLayout());
        volumePricePanel = new VolumePricePanel(volumePriceContainer);
        volumePriceContainer.add(new JLabel("Volume At Price"), BorderLayout.NORTH);
        volumePriceContainer.add(volumePricePanel, BorderLayout.CENTER);

        JPanel volumeProfileContainer = new JPanel(new BorderLayout());
        volumeProfilePanel = new VolumeProfilePanel(volumeProfileContainer, VolumeProfilePanel.ChartType.GROUPED);
        volumeProfileContainer.add(new JLabel("Volume Profile"), BorderLayout.NORTH);
        volumeProfileContainer.add(volumeProfilePanel, BorderLayout.CENTER);

        volumePanel.add(volumePriceContainer);
        volumePanel.add(volumeProfileContainer);

        tablesPanel.add(volumePanel, BorderLayout.CENTER);

        contentPanel.add(tablesPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void addSupply(List<LiquidityLevel> data){

        List<Quote> volumeProfileList = new ArrayList<>();

        for(LiquidityLevel e : data){

            if(e.getPercentile() > 90) {
                Quote quote = new Quote();
                quote.setHigh(e.price);
                quote.setVolume(e.getVolume());
                quote.setPercentile(e.getPercentile());
                volumeProfileList.add(quote);
            }
        }
        volumePricePanel.addQuotes(volumeProfileList);

    }

    public void addVolumeProfile(String symbol, java.util.List<LiquidityLevel> levels){
        List<Quote> volumeProfileList = new ArrayList<>();

        for(LiquidityLevel e : levels){

            Quote quote = new Quote();
            quote.setHigh(e.price);
            quote.setVolume(e.getVolume());
            quote.setPercentile(e.getPercentile());
            volumeProfileList.add(quote);
        }
        volumeProfilePanel.addQuotes(volumeProfileList);
        volumeProfilePanel.addTitle(symbol);
        volumePricePanel.addTitle(symbol);

    }

}
