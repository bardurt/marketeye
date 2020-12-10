package com.zygne.stockanalyzer.domain.interactor.implementation.data.ib;

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

public class IbHistogramInteractor extends BaseInteractor implements HistogramInteractor {
    private static final String DELIM = ",";

    private final Callback callback;
    private final List<BarData> entries;

    public IbHistogramInteractor(Executor executor, MainThread mainThread, Callback callback, List<BarData> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
    }

    @Override
    public void run() {
        List<Histogram> data = new ArrayList<>();

        for (BarData line : entries) {

            try {

                long timeStamp = TimeHelper.getTimeStamp("yyyyMMdd HH:mm:ss", line.getTime());
                double open = line.getOpen();
                double high = line.getHigh();
                double low = line.getLow();
                double close = line.getClose();
                long volume = line.getVolume();

                Histogram histogram = new Histogram();
                histogram.open = NumberHelper.round2Decimals(open);
                histogram.high = NumberHelper.round2Decimals(high);
                histogram.low = NumberHelper.round2Decimals(low);
                histogram.close = NumberHelper.round2Decimals(close);
                histogram.volume = volume;
                histogram.timeStamp = timeStamp;
                data.add(histogram);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mainThread.post(() -> callback.onHistogramCreated(data));

    }

}
