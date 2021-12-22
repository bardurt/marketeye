package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramGroupingInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramGroupingInteractorImpl extends BaseInteractor implements HistogramGroupingInteractor {

    private Callback callback;
    private List<Histogram> histogramList;
    private Group group;
    private final int periods = 9;
    private SimpleMovingAverage sma;

    public HistogramGroupingInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, Group group) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
        this.group = group;
        this.sma = new SimpleMovingAverage(periods);
    }

    @Override
    public void run() {

        final List<Histogram> groupedHistograms;

        System.out.println("Grouping Histograms by " + group.name());
        switch (group) {
            case DAY:
                groupedHistograms = groupByDay(histogramList);
                break;
            case HOUR:
                groupedHistograms = groupByHour(histogramList);
                break;
            default:
                groupedHistograms = groupByDay(histogramList);
        }

        for(int i = 0; i < groupedHistograms.size(); i++){

            Histogram histogram = groupedHistograms.get(i);
            sma.addData(histogram.volume);

            if(i > periods){
                histogram.volumeSma = sma.getMean();
            } else {
                histogram.volumeSma = 0;
            }
        }

        groupedHistograms.sort(new Histogram.VolumeSmaComparator());
        Collections.reverse(groupedHistograms);

        int size = groupedHistograms.size();
        double percentile = 100;

        for (int i = 0; i < groupedHistograms.size(); i++) {
            percentile = ((size-(i)) / (double) size) * 100d;
            groupedHistograms.get(i).volumeSmaPercentile = percentile;
        }

        groupedHistograms.sort(new Histogram.TimeComparator());
        mainThread.post(() -> callback.onHistogramGrouped(groupedHistograms, group));
    }


    private List<Histogram> groupByDay(List<Histogram> histograms) {
        histograms.sort(new Histogram.TimeComparator());
        List<Histogram> histogramList = new ArrayList<>();

        Histogram histogram1 = histograms.get(0);
        double open = histogram1.open;
        double high = histogram1.high;
        double low = histogram1.low;
        double close = histogram1.close;
        long volume = histogram1.volume;
        long timeStamp = histogram1.timeStamp;

        for (int i = 1; i < histograms.size(); i++) {
            Histogram histogram = histograms.get(i);

            if (Histogram.isSameDay(histogram1, histogram)) {

                close = histogram.close;
                if (histogram.low < low) {
                    low = histogram.low;
                }

                if (histogram.high > high) {
                    high = histogram.high;
                }

                volume += histogram.volume;
            } else {
                Histogram group = new Histogram();
                group.open = open;
                group.close = close;
                group.high = high;
                group.low = low;
                group.volume = volume;
                group.timeStamp = timeStamp;

                histogramList.add(group);

                histogram1 = histogram;
                open = histogram1.open;
                high = histogram1.high;
                low = histogram1.low;
                close = histogram1.close;
                volume = histogram1.volume;
                timeStamp = histogram1.timeStamp;
            }

        }

        return histogramList;

    }

    private List<Histogram> groupByHour(List<Histogram> histograms) {
        histograms.sort(new Histogram.TimeComparator());
        List<Histogram> histogramList = new ArrayList<>();

        Histogram histogram1 = histograms.get(0);
        double open = histogram1.open;
        double high = histogram1.high;
        double low = histogram1.low;
        double close = histogram1.close;
        long volume = histogram1.volume;
        long timeStamp = histogram1.timeStamp;

        for (int i = 1; i < histograms.size(); i++) {
            Histogram histogram = histograms.get(i);

            if (Histogram.isSameHour(histogram1, histogram)) {

                close = histogram.close;
                if (histogram.low < low) {
                    low = histogram.low;
                }

                if (histogram.high > high) {
                    high = histogram.high;
                }

                volume += histogram.volume;
            } else {
                Histogram group = new Histogram();
                group.open = open;
                group.close = close;
                group.high = high;
                group.low = low;
                group.volume = volume;
                group.timeStamp = timeStamp;

                histogramList.add(group);

                histogram1 = histogram;
                open = histogram1.open;
                high = histogram1.high;
                low = histogram1.low;
                close = histogram1.close;
                volume = histogram1.volume;
                timeStamp = histogram1.timeStamp;
            }

        }

        return histogramList;
    }

}
