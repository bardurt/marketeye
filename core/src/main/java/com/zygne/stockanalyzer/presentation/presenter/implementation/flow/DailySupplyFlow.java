package com.zygne.stockanalyzer.presentation.presenter.implementation.flow;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.HistogramDayFilterInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramDayFilterInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.VolumeBarDetails;

import java.util.List;

public class DailySupplyFlow implements SupplyFlow.Callback, HistogramDayFilterInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private List<VolumeBarDetails> volumeBarDetails;
    private List<Histogram> histogramList;
    private VolumeBarDetails current;
    private int index = -1;
    private SupplyFlow supplyFlow;

    public DailySupplyFlow(Executor executor, MainThread mainThread, Callback callback, List<VolumeBarDetails> volumeBarDetails, List<Histogram> histogramList) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
        this.volumeBarDetails = volumeBarDetails;
        this.histogramList = histogramList;
        this.supplyFlow = new SupplyFlow(executor, mainThread, this);
    }

    public void start() {
        stepNext();
    }

    private void stepNext() {
        index++;
        current = volumeBarDetails.get(index);

        new HistogramDayFilterInteractorImpl(executor, mainThread, this, histogramList, current.getTimeStamp()).execute();

    }


    @Override
    public void onSupplyCompleted(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        current.setSupply(filtered);
        if(index >= volumeBarDetails.size() -1){
            callback.onDailySupplyFound(volumeBarDetails);
        } else {
            stepNext();
        }
    }

    @Override
    public void onHistogramFiltered(List<Histogram> data) {
        supplyFlow.start(data, 85, 0);
    }


    public interface Callback {
        void onDailySupplyFound(List<VolumeBarDetails> data);
    }
}
