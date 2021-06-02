package com.zygne.stockanalyzer.presentation.presenter.implementation.flow;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.AverageVolumeInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.av.AvFundamentalsInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.AverageVolumeInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.FundamentalsInteractor;
import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.Settings;

import java.util.List;

public class FundamentalsFlow implements FundamentalsInteractor.Callback,
        AverageVolumeInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private final Settings settings;
    private List<Histogram> histogramList;

    private Fundamentals fundamentals;

    public FundamentalsFlow(Executor executor, MainThread mainThread, Callback callback, Settings settings) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
        this.settings = settings;
    }

    public void start(String ticker, List<Histogram> histograms){
        this.histogramList = histograms;
        this.fundamentals = new Fundamentals();

        new AvFundamentalsInteractor(executor, mainThread, this, ticker, settings.getApiKey()).execute();
    }

    @Override
    public void onAverageVolumeCalculated(int period, double avgVol) {
        fundamentals.setAvgVol((long) avgVol);
        this.callback.onFundamentalsPrepared(fundamentals);
    }

    @Override
    public void onFundamentalsFetched(Fundamentals fundamentals) {
        this.fundamentals = fundamentals;
        new AverageVolumeInteractorImpl(executor, mainThread, this, 30, histogramList).execute();
    }

    public interface Callback{
        void onFundamentalsPrepared(Fundamentals fundamentals);
    }
}
