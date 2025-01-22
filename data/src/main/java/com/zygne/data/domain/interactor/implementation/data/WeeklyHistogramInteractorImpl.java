package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.BaseInteractor;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeeklyHistogramInteractorImpl extends BaseInteractor implements WeeklyHistogramInteractor {

    private final Callback callback;
    private final List<Histogram> entries;
    private final SimpleMovingAverage sma;

    public WeeklyHistogramInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
        this.sma = new SimpleMovingAverage(9);
    }

    @Override
    public void run() {
        List<Histogram> data = new ArrayList<>();

        HistogramMerger merger = new HistogramMerger();


        try {

            for (Histogram histogram : entries) {
                if (merger.belongs(histogram)) {
                    merger.add(histogram);
                } else {

                    Histogram weeklyHistogram = merger.merge();
                    sma.addData(weeklyHistogram.volume);
                    weeklyHistogram.volumeSma = (long) sma.getMean();
                    merger.clear();
                    merger.add(histogram);

                    data.add(weeklyHistogram);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        data.sort(new Histogram.VolumeSmaComparator());
        Collections.reverse(data);

        int size = data.size();
        double percentile;

        for (int i = 0; i < data.size(); i++) {
            percentile = ((size - (i)) / (double) size) * 100d;
            data.get(i).volumeSmaPercentile = percentile;
        }

        data.sort(new Histogram.TimeComparator());
        Collections.reverse(data);

        mainThread.post(() -> callback.onWeeklyHistogramCreated(data));

    }

    static final class HistogramMerger {

        private final List<Histogram> items = new ArrayList<>();

        public void add(Histogram histogram) {
            items.add(histogram);
        }

        public void clear() {
            items.clear();
        }

        public boolean belongs(Histogram h) {
            if (items.isEmpty()) {
                return true;
            }

            return items.get(0).isSameWeek(h.timeStamp);
        }

        public Histogram merge() {

            items.sort(new Histogram.TimeComparator());
            Histogram mergedHistogram = new Histogram();

            mergedHistogram.close = items.getLast().close;
            mergedHistogram.open = items.getFirst().open;

            double high = -1000000;
            double low = Double.MAX_VALUE;
            long volume = 0;

            for (Histogram histogram : items) {
                if (histogram.high > high) {
                    high = histogram.high;
                }

                if (histogram.low < low) {
                    low = histogram.low;
                }

                volume += histogram.volume;
            }

            mergedHistogram.high = high;
            mergedHistogram.low = low;
            mergedHistogram.volume = volume;
            mergedHistogram.timeStamp = items.get(0).timeStamp;

            return mergedHistogram;
        }

    }

}


