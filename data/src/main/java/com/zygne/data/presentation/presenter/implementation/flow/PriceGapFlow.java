package com.zygne.data.presentation.presenter.implementation.flow;

import com.zygne.data.domain.interactor.implementation.data.PriceGapInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.base.PriceGapInteractor;
import com.zygne.data.domain.model.GapHistory;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.PriceGap;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;

import java.util.List;

public class PriceGapFlow implements PriceGapInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private List<Histogram> histogramList;
    private final GapType gapType;

    public PriceGapFlow(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, GapType gapType) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
        this.histogramList = histogramList;
        this.gapType = gapType;
    }

    public void start() {
        new PriceGapInteractorImpl(executor, mainThread, this, histogramList).execute();
    }

    @Override
    public void onPriceGapsFound(List<PriceGap> data) {
        callback.onPriceGapsGenerated(data, gapType);
    }


    public interface Callback {
        void onPriceGapsGenerated(List<PriceGap> data, GapType gapType);
    }

    public enum GapType {
        DAILY,
    }
}
