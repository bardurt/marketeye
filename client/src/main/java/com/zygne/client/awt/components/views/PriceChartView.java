package com.zygne.client.awt.components.views;

import com.zygne.chart.chart.charts.pricechart.PricePanel;
import com.zygne.chart.chart.model.data.Quote;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.LiquidityLevel;
import com.zygne.data.domain.model.PriceGap;
import com.zygne.data.domain.model.PriceImbalance;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriceChartView extends JPanel {

    private PricePanel pricePanel = new PricePanel(this);

    public PriceChartView() {
        setLayout(new BorderLayout());
        add(pricePanel, BorderLayout.CENTER);
    }

    public void addData(java.util.List<Histogram> data, String symbol) {

        java.util.List<Quote> quoteList = new ArrayList<>();

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
        java.util.List<Quote> volumeProfileList = new ArrayList<>();

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
        java.util.List<Quote> volumeProfileList = new ArrayList<>();

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
        java.util.List<Quote> volumeProfileList = new ArrayList<>();

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

    public void addSupply(java.util.List<LiquidityLevel> data) {

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