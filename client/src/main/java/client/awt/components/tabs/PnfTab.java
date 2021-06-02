package client.awt.components.tabs;

import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.zchart.volumeprofile.PointAndFigurePanel;
import com.zygne.zchart.volumeprofile.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PnfTab extends JPanel {

    private PointAndFigurePanel pointAndFigurePanel;

    public PnfTab() {
        setLayout(new BorderLayout());

        pointAndFigurePanel = new PointAndFigurePanel(this);
        add(pointAndFigurePanel);
    }

    public void addVolumeProfile(String symbol, java.util.List<Histogram> histograms){
        List<Quote> volumeProfileList = new ArrayList<Quote>();

        for(Histogram e : histograms){

            Quote quote = new Quote();
            quote.setOpen(e.open);
            quote.setHigh(e.high);
            quote.setLow(e.low);
            quote.setClose(e.close);
            quote.setVolume(e.volume);
            quote.setTimeStamp(e.timeStamp);
            volumeProfileList.add(quote);
        }
        pointAndFigurePanel.addQuotes(volumeProfileList);

    }

}
