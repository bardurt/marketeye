package com.zygne.client.awt.components.views;


import com.zygne.data.domain.model.Tendency;
import com.zygne.data.domain.model.TendencyReport;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LineChart extends JPanel {

    private String[] colors = new String[]{"#D93333", "#2A9AB2", "#23BE59", "#E3A50C", "#BC0349"};
    private long x1 = 0;
    private long x2 = 0;
    private long q1 = 0;
    private long q2 = 0;
    private long q3 = 0;

    public LineChart() {
        this.setLayout(new GridLayout());
    }

    public void addTendency(String symbol, TendencyReport tendencyReport) {

        removeAll();

        q1 = tendencyReport.getQuarter(1);
        q2 = tendencyReport.getQuarter(2);
        q3 = tendencyReport.getQuarter(3);
        var dataset = new TimeSeriesCollection();

        for (Tendency t : tendencyReport.tendencies()) {

            var series = new TimeSeries(t.name);

            for (int i = 0; i < t.data.size(); i++) {

                TimeSeriesDataItem item = new TimeSeriesDataItem(new Day(new Date(t.data.get(i).timeStamp)), t.data.get(i).value);
                series.add(item);
            }

            x1 = series.getTimePeriod(0).getFirstMillisecond();
            x2 = series.getNextTimePeriod().getLastMillisecond();


            System.out.println("Series size " + series.getTimePeriods().size());


            dataset.addSeries(series);
        }

        JFreeChart chart = createChart(symbol, dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        invalidate();
        validate();
        repaint();
    }


    private JFreeChart createChart(String symbol, XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        DateAxis dateAxis = new DateAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM"));
        plot.setDomainAxis(dateAxis);
        plot.setDomainMinorGridlinesVisible(false);

        Marker m1 = new ValueMarker(q1);
        m1.setStroke(new BasicStroke(1));
        m1.setPaint(Color.BLACK);
        plot.addDomainMarker(m1);

        Marker m2 = new ValueMarker(q2);
        m2.setStroke(new BasicStroke(1));
        m2.setPaint(Color.BLACK);
        plot.addDomainMarker(m2);

        Marker m3 = new ValueMarker(q3);
        m3.setStroke(new BasicStroke(1));
        m3.setPaint(Color.BLACK);
        plot.addDomainMarker(m3);

        var renderer = new XYLineAndShapeRenderer(true, false);
        for (int i = 0; i < 5; i++) {
            renderer.setSeriesStroke(i, new BasicStroke(2f));
            renderer.setSeriesPaint(i, Color.decode(colors[i]));
        }

        double y = 0;
        XYLineAnnotation line = new XYLineAnnotation(
                x1, y, x2, y, new BasicStroke(1.5f), Color.DARK_GRAY);
        plot.addAnnotation(line);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        String name = "";

        if (symbol != null) {
            name = symbol;
        }

        chart.setTitle(new TextTitle("Seasonality " + name,
                        new Font("Serif", java.awt.Font.BOLD, 20)
                )
        );

        return chart;
    }
}