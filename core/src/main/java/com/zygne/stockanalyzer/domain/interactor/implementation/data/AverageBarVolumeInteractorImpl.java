package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.AverageBarVolumeInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;

import java.util.List;

public class AverageBarVolumeInteractorImpl extends BaseInteractor implements AverageBarVolumeInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;

    public AverageBarVolumeInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
    }

    @Override
    public void run() {

        long volumeSum = 0;

        try {
            for (Histogram h : histogramList) {
                volumeSum += h.volume;
            }

        } catch (Exception ignored) {

        }

        final long avgVol = volumeSum / histogramList.size();
        mainThread.post(() -> callback.onAverageBarVolumeCalculated(avgVol));

    }
}
