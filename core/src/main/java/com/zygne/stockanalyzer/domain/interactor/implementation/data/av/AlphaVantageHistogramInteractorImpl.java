package com.zygne.stockanalyzer.domain.interactor.implementation.data.av;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.utils.NumberHelper;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.List;

public class AlphaVantageHistogramInteractorImpl extends BaseInteractor implements HistogramInteractor {
    private static final String DELIM = ",";

    private final Callback callback;
    private final List<String> entries;

    public AlphaVantageHistogramInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<String> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
    }

    @Override
    public void run() {
        String[] tempArr;
        List<Histogram> data = new ArrayList<>();

        for (String line : entries) {

            tempArr = line.split(DELIM);

            try {

                long timeStamp = TimeHelper.getTimeStamp(tempArr[0]);
                double open = Double.parseDouble(tempArr[1]);
                double high = Double.parseDouble(tempArr[2]);
                double low = Double.parseDouble(tempArr[3]);
                double close = Double.parseDouble(tempArr[4]);
                long volume = Long.parseLong(tempArr[5]);

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
