package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.sun.xml.internal.rngom.parse.host.Base;
import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;
import com.zygne.stockalyze.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramInteractorImpl extends BaseInteractor implements HistogramInteractor {
    private static final String delimiter = ",";

    private final Callback callback;
    private final List<String> entries;

    public HistogramInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<String> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
    }


    @Override
    public void run() {
        String[] tempArr;
        List<Histogram> data = new ArrayList<>();
        int count = 0;
        for (String line : entries) {

            tempArr = line.split(delimiter);

            try {

                long volume = Long.parseLong(tempArr[5]);
                double open = Double.parseDouble(tempArr[1]);
                double high = Double.parseDouble(tempArr[2]);
                double low = Double.parseDouble(tempArr[3]);
                double close = Double.parseDouble(tempArr[4]);

                Histogram histogram = new Histogram();
                histogram.open = (int) (open * 100);
                histogram.high = (int) (high * 100);
                histogram.low = (int) (low * 100);
                histogram.close = (int) (close * 100);
                histogram.volume = volume;
                histogram.timeStamp = count+1;
                data.add(histogram);

            } catch (Exception e) {
                System.out.println("Error at line " + count + " : " + e.getMessage());
            }

            count++;
        }

        mainThread.post(() -> callback.onHistogramCreated(data));

    }

}
