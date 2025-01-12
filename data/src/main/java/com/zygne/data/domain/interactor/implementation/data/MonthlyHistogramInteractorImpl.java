package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonthlyHistogramInteractorImpl extends BaseInteractor implements MonthlyHistogramInteractor {

    private final Callback callback;
    private final List<Histogram> entries;
    private final SimpleMovingAverage sma;
    private final int periods = 9;

    public MonthlyHistogramInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
        this.sma = new SimpleMovingAverage(periods);
    }

    @Override
    public void run() {
        List<Histogram> data = new ArrayList<>();

        HistogramMerger merger = new HistogramMerger();


        try {

            for(int i = 0; i < entries.size(); i++){
                Histogram histogram = entries.get(i);
                if (merger.belongs(histogram)) {
                    merger.add(histogram);
                } else {

                    Histogram monthlyHistogram = merger.merge();
                    sma.addData(monthlyHistogram.volume);
                    monthlyHistogram.volumeSma = (long) sma.getMean();
                    merger.clear();
                    merger.add(histogram);

                    data.add(monthlyHistogram);
                }

            }
        } catch (Exception e){
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

        mainThread.post(() -> callback.onMonthlyHistogramCreated(data));

    }

    static final class HistogramMerger {

        private List<Histogram> items = new ArrayList<>();

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

            if (items.get(0).isSameMonth(h.timeStamp)) {
                return true;
            }

            return false;
        }

        public Histogram merge() {

            items.sort(new Histogram.TimeComparator());
            Histogram h = new Histogram();

            h.close = items.getLast().close;
            h.open = items.getFirst().open;

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

            h.high = high;
            h.low = low;
            h.volume = volume;
            h.timeStamp = items.get(0).timeStamp;

            System.out.println("Merging monthly bar, item count " + items.size());

            return h;
        }

    }

}
