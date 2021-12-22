package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.SimpleMovingAverage;
import com.zygne.stockanalyzer.domain.utils.NumberHelper;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramInteractorImpl extends BaseInteractor implements HistogramInteractor {

    private final Callback callback;
    private final List<BarData> entries;
    private final SimpleMovingAverage sma;
    private final int periods = 9;

    public HistogramInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<BarData> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
        this.sma = new SimpleMovingAverage(periods);
    }

    @Override
    public void run() {
        List<Histogram> data = new ArrayList<>();

        int count = 0;
        for (BarData line : entries) {

            try {

                count++;
                long timeStamp = line.getTimeStamp();
                double open = NumberHelper.round2Decimals(line.getOpen());
                double high = NumberHelper.round2Decimals(line.getHigh());
                double low = NumberHelper.round2Decimals(line.getLow());
                double close = NumberHelper.round2Decimals(line.getClose());
                long volume = line.getVolume();
                String dateTime = TimeHelper.getDateFromTimeStamp(timeStamp);

                Histogram histogram = new Histogram();
                histogram.open = open;
                histogram.high = high;
                histogram.low = low;
                histogram.close = close;
                histogram.volume = volume;
                histogram.timeStamp = timeStamp;
                histogram.dateTime = dateTime;

                sma.addData(volume);

                if(count > periods){
                    histogram.volumeSma = sma.getMean();
                } else {
                    histogram.volumeSma = 0;
                }

                data.add(histogram);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        data.sort(new Histogram.VolumeSmaComparator());
        Collections.reverse(data);

        int size = data.size();
        double percentile = 100;

        for (int i = 0; i < data.size(); i++) {
            percentile = ((size-(i)) / (double) size) * 100d;
            data.get(i).volumeSmaPercentile = percentile;
        }

        data.sort(new Histogram.TimeComparator());
        Collections.reverse(data);

        mainThread.post(() -> callback.onHistogramCreated(data));

    }

}
