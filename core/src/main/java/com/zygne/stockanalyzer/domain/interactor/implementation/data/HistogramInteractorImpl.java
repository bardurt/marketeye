package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.utils.NumberHelper;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.List;

public class HistogramInteractorImpl extends BaseInteractor implements HistogramInteractor {
    private static final String DELIM = ",";

    private final Callback callback;
    private final List<BarData> entries;

    public HistogramInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<BarData> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
    }

    @Override
    public void run() {
        List<Histogram> data = new ArrayList<>();

        for (BarData line : entries) {

            try {

                long timeStamp = line.getTimeStamp();
                double open =  NumberHelper.round2Decimals(line.getOpen());
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
                data.add(histogram);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mainThread.post(() -> callback.onHistogramCreated(data));

    }

}
