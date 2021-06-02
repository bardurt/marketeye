package com.zygne.zchart;

import com.zygne.zchart.volumeprofile.PointAndFigurePanel;
import com.zygne.zchart.volumeprofile.VolumeProfilePanel;
import com.zygne.zchart.volumeprofile.model.data.Quote;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChartView2 extends JPanel {

     VolumeProfilePanel chartPanel;

    public ChartView2() {
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());
        chartPanel = new VolumeProfilePanel(this, VolumeProfilePanel.ChartType.GROUPED);
        chartPanel.addQuotes(generateQuotesVp(generateData()));
        add(chartPanel, BorderLayout.CENTER);
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
}
