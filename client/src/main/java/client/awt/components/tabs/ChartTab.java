package client.awt.components.tabs;

import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.model.PriceImbalance;
import com.zygne.zchart.chart.charts.pricechart.PricePanel;
import com.zygne.zchart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartTab extends JPanel {

    private PricePanel pricePanel = new PricePanel(this);

    public ChartTab(Callback callback) {

        JButton button = new JButton("Create Chart");
        button.addActionListener(e -> callback.createChart());

        setLayout(new BorderLayout());
        add(pricePanel, BorderLayout.CENTER);
    }

    public void addData(List<Histogram> data, String symbol) {

        List<Quote> quoteList = new ArrayList<>();

        for (Histogram e : data) {

            Quote quote = new Quote();
            quote.setHigh(e.high);
            quote.setOpen(e.open);
            quote.setLow(e.low);
            quote.setClose(e.close);
            quote.setVolume(e.volume);
            quote.setTimeStamp(e.timeStamp);
            quote.setVolumeSma(e.volumeSma);
            quote.setVolumeSmaPercentile(e.volumeSmaPercentile);
            quoteList.add(quote);
        }
        pricePanel.addQuotes(quoteList);
        pricePanel.addWaterMark(symbol);
    }

    public void addVolumeProfile(java.util.List<LiquidityLevel> levels) {
        List<Quote> volumeProfileList = new ArrayList<>();

        for (LiquidityLevel e : levels) {

            Quote quote = new Quote();
            quote.setHigh(e.price);
            quote.setVolume(e.getVolume());
            quote.setPercentile(e.getPercentile());
            volumeProfileList.add(quote);
        }
        pricePanel.addVolumeProfile(volumeProfileList);

    }


    public void addPriceGaps(java.util.List<PriceGap> levels) {
        List<Quote> volumeProfileList = new ArrayList<>();

        for (PriceGap e : levels) {

            if (!e.isFilled()) {
                Quote quote = new Quote();
                quote.setHigh(e.getEnd());
                quote.setLow(e.getStart());
                quote.setTimeStamp(e.getTimeStamp());
                quote.setIndex(e.getIndex());
                volumeProfileList.add(quote);
            }

        }
        pricePanel.addPriceGaps(volumeProfileList);

    }

    public void addPriceImbalances(java.util.List<PriceImbalance> levels) {
        List<Quote> volumeProfileList = new ArrayList<>();

        for (PriceImbalance e : levels) {
            Quote quote = new Quote();
            quote.setHigh(e.getEnd());
            quote.setLow(e.getStart());
            quote.setTimeStamp(e.getTimeStamp());
            quote.setIndex(e.getIndex());
            volumeProfileList.add(quote);
        }
        pricePanel.addPriceImbalances(volumeProfileList);

    }

    public void addSupply(List<LiquidityLevel> data) {

        List<Quote> volumeProfileList = new ArrayList<>();

        for (LiquidityLevel e : data) {

            if (e.getPercentile() > 90) {
                Quote quote = new Quote();
                quote.setHigh(e.price);
                quote.setVolume(e.getVolume());
                quote.setPercentile(e.getPercentile());
                volumeProfileList.add(quote);
            }
        }
        pricePanel.addPricePressure(volumeProfileList);

    }

    public interface Callback {
        void createChart();
    }
}
