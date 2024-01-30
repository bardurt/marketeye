package com.zygne.client.awt.components.views;


import com.zygne.data.domain.model.TendencyReport;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
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

    private String[] colors = new String[]{"#A2A90E", "#2A9AB2", "#23BE59", "#E3A50C", "#BC0349"};
    private long x1 = 0;
    private long x2 = 0;

    public LineChart() {
        this.setLayout(new GridLayout());
    }

    public void addTendency(String symbol, TendencyReport tendencyReport) {

        removeAll();
        var dataset = new TimeSeriesCollection();

        if (tendencyReport.currentYear != null) {
            var series = new TimeSeries("Current Year");

            for (int i = 0; i < tendencyReport.currentYear.size(); i++) {

                TimeSeriesDataItem item = new TimeSeriesDataItem(new Day(new Date(tendencyReport.currentYear.get(i).timeStamp)), tendencyReport.currentYear.get(i).avg);
                series.add(item);
            }

            dataset.addSeries(series);
        }

        if (tendencyReport.fiveYear != null) {
            var series = new TimeSeries("5 Year");

            for (int i = 0; i < tendencyReport.fiveYear.size(); i++) {

                TimeSeriesDataItem item = new TimeSeriesDataItem(new Day(new Date(tendencyReport.fiveYear.get(i).timeStamp)), tendencyReport.fiveYear.get(i).avg);
                series.add(item);
            }

            x1 = series.getTimePeriod(0).getFirstMillisecond();
            x2 = series.getNextTimePeriod().getLastMillisecond();
            dataset.addSeries(series);
        }

        if (tendencyReport.tenYear != null) {
            var series = new TimeSeries("10 Year");

            for (int i = 0; i < tendencyReport.tenYear.size(); i++) {
                TimeSeriesDataItem item = new TimeSeriesDataItem(new Day(new Date(tendencyReport.tenYear.get(i).timeStamp)), tendencyReport.tenYear.get(i).avg);
                series.add(item);
            }

            dataset.addSeries(series);

        }

        if (tendencyReport.fifteenYear != null) {
            var series = new TimeSeries("15 Year");

            for (int i = 0; i < tendencyReport.fifteenYear.size(); i++) {
                TimeSeriesDataItem item = new TimeSeriesDataItem(new Day(new Date(tendencyReport.fifteenYear.get(i).timeStamp)), tendencyReport.fifteenYear.get(i).avg);
                series.add(item);
            }

            dataset.addSeries(series);
        }

        if (tendencyReport.twentyYear != null) {
            var series = new TimeSeries("20 Year");

            for (int i = 0; i < tendencyReport.twentyYear.size(); i++) {
                TimeSeriesDataItem item = new TimeSeriesDataItem(new Day(new Date(tendencyReport.twentyYear.get(i).timeStamp)), tendencyReport.twentyYear.get(i).avg);
                series.add(item);
            }

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

        chart.setTitle(new TextTitle("Seasonality " + symbol,
                        new Font("Serif", java.awt.Font.BOLD, 20)
                )
        );

        return chart;
    }

    public class LineRenderer extends XYLineAndShapeRenderer {


    }
}