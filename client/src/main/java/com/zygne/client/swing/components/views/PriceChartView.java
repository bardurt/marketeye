package com.zygne.client.swing.components.views;

import com.zygne.chart.chart.charts.pricechart.PriceChart;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.chart.chart.model.data.VolumeSerie;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.LiquidityLevel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriceChartView extends JPanel {

    private final PriceChart pricePanel = new PriceChart();

    public PriceChartView() {
        setLayout(new BorderLayout());
        add(pricePanel, BorderLayout.CENTER);
    }

    public void addData(List<Histogram> data, List<LiquidityLevel> levels, String symbol) {

        List<Serie> quoteList = new ArrayList<>();

        for (Histogram e : data) {

            BarSerie quote = new BarSerie();
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

        List<Serie> volumeProfileList = new ArrayList<>();

        for (LiquidityLevel e : levels) {

            VolumeSerie quote = new VolumeSerie();
            quote.setPrice(e.price);
            quote.setVolume(e.getVolume());
            volumeProfileList.add(quote);
        }

        series.add(volumeProfileList);

        pricePanel.setSeries(series);
        pricePanel.setWaterMark(symbol);
    }

}