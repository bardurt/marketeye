package com.zygne.stockanalyzer.presentation.presenter.implementation;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.av.AlphaVantageHistogramInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.CacheCheckerInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.CacheReadInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.PriceGapInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.BasePresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.PriceGapPresenter;

import java.util.List;

public class PriceGapPresenterImpl extends BasePresenter implements PriceGapPresenter,
        PriceGapInteractor.Callback {

    private View view;
    private Settings settings;

    public PriceGapPresenterImpl(Executor executor, MainThread mainThread, View view, Settings settings) {
        super(executor, mainThread);
        this.view = view;
        this.settings = settings;
    }

    @Override
    public void findGaps(List<Histogram> histograms) {
        view.showLoading("Analyzing gaps...");
        new PriceGapInteractorImpl(executor, mainThread, this, histograms).execute();
    }

    @Override
    public void onPriceGapsFound(List<PriceGap> data) {
        view.hideLoading();
        view.onPriceGapsFound(data);
    }
}
