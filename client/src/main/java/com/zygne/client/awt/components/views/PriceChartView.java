package com.zygne.client.awt.components.views;

import com.zygne.chart.chart.charts.pricechart.PricePanel;
import com.zygne.chart.chart.model.data.Quote;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.LiquidityLevel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriceChartView extends JPanel {

    private final PricePanel pricePanel = new PricePanel(this);

    public PriceChartView() {
        setLayout(new BorderLayout());
        add(pricePanel, BorderLayout.CENTER);
    }

    public void addData(List<Histogram> data, String symbol) {

        List<Serie> quoteList = new ArrayList<>();

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

        List<List<Serie>> series = new ArrayList<>();
        series.add(quoteList);
        pricePanel.addSeries(series);
        pricePanel.addWaterMark(symbol);
    }

    public void addVolumeProfile(List<LiquidityLevel> levels) {
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
}