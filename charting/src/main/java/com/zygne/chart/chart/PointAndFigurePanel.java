package com.zygne.chart.chart;

import com.zygne.chart.chart.model.chart.AwtCanvas;
import com.zygne.chart.chart.model.chart.Canvas;
import com.zygne.chart.chart.model.data.Quote;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PointAndFigurePanel extends JPanel {

    private PointAndFigureChart chart;
    private JPanel parent;

    public PointAndFigurePanel(JPanel parent) {
        this.parent = parent;
        this.chart = new PointAndFigureChart(this);
        addMouseMotionListener(chart);
        addMouseListener(chart);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background

        Canvas canvas = new AwtCanvas(g);
        chart.draw(canvas);
    }

    private List<Quote> generateQuotes(){

        List<Quote> quoteList = new ArrayList<>();

        Quote q1 = new Quote();
        q1.setClose(90);

        quoteList.add(q1);

        Quote q2 = new Quote();
        q2.setClose(91);

        quoteList.add(q2);

        Quote q3 = new Quote();
        q3.setClose(92);

        quoteList.add(q3);

        Quote q4 = new Quote();
        q4.setClose(93);

        quoteList.add(q4);

        Quote q5 = new Quote();
        q5.setClose(90);

        quoteList.add(q5);

        Quote q6 = new Quote();
        q6.setClose(89);

        quoteList.add(q6);

        Quote q7 = new Quote();
        q7.setClose(88);

        quoteList.add(q7);

        Quote q8 = new Quote();
        q8.setClose(87);

        quoteList.add(q8);

        Quote q9 = new Quote();
        q9.setClose(90);

        quoteList.add(q9);

        Quote q10 = new Quote();
        q10.setClose(91);

        quoteList.add(q10);

        return quoteList;
    }

    public void addQuotes(List<Quote> quotes){
        chart.setBars(quotes);
        parent.repaint();
    }
}
