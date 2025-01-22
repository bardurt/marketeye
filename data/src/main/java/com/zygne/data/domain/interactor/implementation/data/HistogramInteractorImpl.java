package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.Logger;
import com.zygne.data.domain.model.BarData;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.SimpleMovingAverage;
import com.zygne.data.domain.utils.TimeHelper;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.BaseInteractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramInteractorImpl extends BaseInteractor implements HistogramInteractor {

    private final Callback callback;
    private final List<BarData> entries;
    private final SimpleMovingAverage sma;
    private final Logger logger;

    public HistogramInteractorImpl(Executor executor, MainThread mainThread, Callback callback, Logger logger, List<BarData> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
        this.sma = new SimpleMovingAverage(9);
        this.logger = logger;
    }

    @Override
    public void run() {
        List<Histogram> data = new ArrayList<>();

        for (BarData line : entries) {

            try {

                long timeStamp = line.getTimeStamp();
                double open = line.open();
                double high = line.high();
                double low = line.low();
                double close = line.close();
                long volume = line.volume();
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
                histogram.volumeSma = (long) sma.getMean();

                data.add(histogram);

            } catch (Exception e) {
                logger.log(Logger.LOG_LEVEL.DEBUG, "Error while creating histogram e = " + e.getMessage());
            }
        }

        data.sort(new Histogram.VolumeSmaComparator().reversed());
        Collections.reverse(data);

        int size = data.size();
        double percentile;

        for (int i = 0; i < data.size(); i++) {
            percentile = ((size - (i)) / (double) size) * 100d;
            data.get(i).volumeSmaPercentile = percentile;
        }

        data.sort(new Histogram.TimeComparator().reversed());

        mainThread.post(() -> callback.onHistogramCreated(data));
    }

}
