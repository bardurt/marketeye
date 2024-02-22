package com.zygne.client.swing.components.views;

import com.zygne.chart.chart.charts.pricechart.PriceChart;
import com.zygne.chart.chart.model.data.CandleSerie;
import com.zygne.chart.chart.model.data.Serie;
import com.zygne.data.domain.model.Histogram;

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

    public void addData(List<Histogram> data, String symbol) {

        List<Serie> quoteList = new ArrayList<>();

        for (Histogram e : data) {

            CandleSerie quote = new CandleSerie();
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

        pricePanel.setSeries(series);
        pricePanel.setWaterMark(symbol);
    }

}