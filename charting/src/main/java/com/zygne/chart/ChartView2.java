package com.zygne.chart;

import com.zygne.chart.chart.charts.pricechart.PricePanel;
import com.zygne.chart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartView2 extends JPanel {

     private PricePanel chartPanel;
     private JButton buttonZoomIn;
     private JButton buttonZoomOut;

    public ChartView2() {
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());
        chartPanel = new PricePanel(this);
        chartPanel.addQuotes(generateBars());
        add(chartPanel, BorderLayout.CENTER);
        chartPanel.addWaterMark("Hello");

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

    private List<Quote> generateQuotesVp(ArrayList<Float> values){

        List<Quote> quoteList = new ArrayList<>();

        for(int i = 0; i < values.size(); i++){
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


    private List<Quote> generateQuotes(){

        List<Quote> quoteList = new ArrayList<>();
        int timeStamp = 1;
        int last = 0;

        for(int i = 0; i < 10; i ++){
            Quote q = new Quote();

            last = 80 + i;
            q.setHigh(last);
            q.setLow(last- 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        for(int i = 0; i < 5; i ++){
            Quote q = new Quote();

            last--;
            q.setHigh(last);
            q.setLow(last- 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        for(int i = 0; i < 4; i ++){
            Quote q = new Quote();
            last++;
            q.setHigh(last);
            q.setLow(last- 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        for(int i = 0; i < 8; i ++){
            Quote q = new Quote();
            last--;
            q.setHigh(last);
            q.setLow(last- 1);
            q.setTimeStamp(timeStamp);

            quoteList.add(q);

            timeStamp++;
        }

        return quoteList;
    }

    private List<Quote> generateBars(){
        List<Quote> quoteList = new ArrayList<>();

        Quote q1 = new Quote();
        q1.setOpen(10.11);
        q1.setHigh(11.01);
        q1.setLow(9.55);
        q1.setClose(9.8);

        quoteList.add(q1);

        Quote q2 = new Quote();
        q2.setOpen(9.90);
        q2.setHigh(10.1);
        q2.setLow(8.2);
        q2.setClose(9.5);

        quoteList.add(q2);

        Quote q3 = new Quote();
        q3.setOpen(9.5);
        q3.setHigh(10.5);
        q3.setLow(7);
        q3.setClose(8.5);

        quoteList.add(q3);

        Quote q4 = new Quote();
        q4.setOpen(9);
        q4.setHigh(12.5);
        q4.setLow(8.5);
        q4.setClose(10);

        quoteList.add(q4);


        return quoteList;
    }
}
