package com.zygne.chart;

import com.zygne.chart.chart.charts.linechart.LinePanel;
import com.zygne.chart.chart.charts.pricechart.PricePanel;
import com.zygne.chart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartView2 extends JPanel {

    private LinePanel chartPanel;
    private JButton buttonZoomIn;
    private JButton buttonZoomOut;

    public ChartView2() {
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());
        chartPanel = new LinePanel(this);
        chartPanel.addQuotes(generateBars());
        add(chartPanel, BorderLayout.CENTER);

        buttonZoomIn = new JButton("Zoom in");
    }

    private ArrayList<Float> generateData() {
        ArrayList<Float> data = new ArrayList<>();

        data.add(44.14f);
        data.add(48.02f);
        data.add(48.85f);
        data.add(56.56f);

        data.add(52.45f);
        data.add(46.26f);

        data.add(49.82f);
        data.add(49.0f);
        data.add(50.07f);
        data.add(51.00f);

        data.add(42.97f);
        data.add(42.14f);
        data.add(38.93f);
        data.add(35.55f);
        data.add(33.57f);
        data.add(34.93f);
        data.add(31.06f);


        return data;
    }

    private List<Quote> generateQuotesVp(ArrayList<Float> values) {

        List<Quote> quoteList = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            Quote q = new Quote();
            q.setLow(values.get(i));
            q.setHigh(values.get(i));
            q.setOpen(values.get(i));
            q.setClose(values.get(i));
            q.setVolume(i * 100);
            quoteList.add(q);
        }

        return quoteList;
    }


    private List<Quote> generateQuotes() {

        List<Quote> quoteList = new ArrayList<>();
        int timeStamp = 1;
        int last = 0;

        for (int i = 0; i < 10; i++) {
            Quote q = new Quote();

            last = 80 + i;
            q.setHigh(last);
            q.setLow(last - 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        for (int i = 0; i < 5; i++) {
            Quote q = new Quote();

            last--;
            q.setHigh(last);
            q.setLow(last - 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        for (int i = 0; i < 4; i++) {
            Quote q = new Quote();
            last++;
            q.setHigh(last);
            q.setLow(last - 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        for (int i = 0; i < 8; i++) {
            Quote q = new Quote();
            last--;
            q.setHigh(last);
            q.setLow(last - 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        return quoteList;
    }

    private List<List<Quote>> generateBars() {

        List<List<Quote>> data = new ArrayList<>();

        double startPrice = 10;

        for (int i = 0; i < 4; i++) {
            startPrice = 10 + i;
            List<Quote> quoteList = new ArrayList<>();
            for (int j = 0; j < 20; j++) {


                Quote q1 = new Quote();
                q1.setOpen(j);
                q1.setHigh(j);
                q1.setLow(j);
                q1.setClose(startPrice);
                quoteList.add(q1);
                startPrice += 0.5;
            }
            data.add(quoteList);
        }

        return data;
    }
}
