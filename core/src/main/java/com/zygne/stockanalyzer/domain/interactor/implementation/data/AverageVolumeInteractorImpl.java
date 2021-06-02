package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.AverageVolumeInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.utils.SimpleMovingAverage;

import java.util.List;

public class AverageVolumeInteractorImpl extends BaseInteractor implements AverageVolumeInteractor {

    private Callback callback;
    private int periods;
    private List<Histogram> histogramList;

    public AverageVolumeInteractorImpl(Executor executor, MainThread mainThread, Callback callback, int periods, List<Histogram> histogramList) {
        super(executor, mainThread);
        this.callback = callback;
        this.periods = periods;
        this.histogramList = histogramList;
    }

    @Override
    public void run() {

        SimpleMovingAverage sma = new SimpleMovingAverage(periods);

        for(Histogram e : histogramList){
            sma.addData(e.volume);
        }

        mainThread.post(() -> callback.onAverageVolumeCalculated(periods, sma.getMean()));
    }
}
