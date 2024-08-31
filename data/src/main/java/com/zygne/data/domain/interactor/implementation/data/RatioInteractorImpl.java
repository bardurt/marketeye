package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;
import com.zygne.data.FileWriter;
import com.zygne.data.domain.interactor.implementation.data.base.BiasInteractor;
import com.zygne.data.domain.interactor.implementation.data.base.RatioInteractor;
import com.zygne.data.domain.model.Bias;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.Ratio;
import com.zygne.data.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.List;

public class RatioInteractorImpl extends BaseInteractor implements RatioInteractor {

    private final List<Histogram> histogramList;
    private final Callback callback;

    public RatioInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
    }

    @Override
    public void run() {
        histogramList.sort(new Histogram.TimeComparator());

        int downClose = 0;
        int upClose = 0;
        int bullBars = 0;
        int bearBars = 0;

        for (int i = 1; i < histogramList.size(); i++) {

            Histogram current = histogramList.get(i);
            Histogram previous = histogramList.get(i - 1);

            if (current.close > current.open) {
                bullBars++;
            } else {
                bearBars++;
            }

            if (current.close > previous.close) {
                upClose++;
            } else {
                downClose++;
            }

        }

        Ratio ratio = new Ratio(upClose, downClose, bullBars, bearBars);

        FileWriter fileWriter = new FileWriter("stats");
        fileWriter.writeLine("Total: " + histogramList.size());
        fileWriter.writeLine("Close Above Previous: " + upClose);
        fileWriter.writeLine("Close Below or at Previous: " + downClose);
        fileWriter.writeLine("Bullish: " + bullBars);
        fileWriter.writeLine("Bearish: " + bearBars);

        fileWriter.close();

        mainThread.post(() -> callback.onRatioCreated(ratio));
    }

}