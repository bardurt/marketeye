package com.zygne.stockanalyzer.presentation.presenter.implementation;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.*;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.BasePresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.LiquiditySidePresenter;

import java.util.List;

public class LiquiditySidePresenterImpl extends BasePresenter implements LiquiditySidePresenter,
        CacheCheckerInteractor.Callback,
        CacheWriteInteractor.Callback,
        CacheReadInteractor.Callback,
        HistogramInteractor.Callback,
        LiquiditySideInteractor.Callback,
        LiquiditySideFilterInteractor.Callback,
        LiquiditySidePriceInteractor.Callback {

    private final View view;
    private final Settings settings;
    private double percentile;
    private double minSize;
    private double priceMin;
    private double priceMax;

    public LiquiditySidePresenterImpl(Executor executor, MainThread mainThread, View view, Settings settings) {
        super(executor, mainThread);
        this.view = view;
        this.settings = settings;
    }

    @Override
    public void getSides(String ticker, TimeInterval timeInterval, double size, double percentile, double priceMin, double priceMax) {
        this.percentile = percentile;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.minSize = size;
        view.showError("");
        view.showLoading("Fetching liquidity sides");
        new CacheCheckerInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeInterval.name()).execute();
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        view.showLoading("Analyzing sides...");
        new LiquiditySideInteractorImpl(executor, mainThread, this, data, minSize).execute();
    }

    @Override
    public void onLiquiditySidesCreated(List<LiquiditySide> data) {
        new LiquiditySideFilterInteractorImpl(executor, mainThread, this, data, percentile).execute();
    }


    @Override
    public void onCachedDataFound(String location) {
        view.showLoading("Reading cache....");
        new CacheReadInteractorImpl(executor, mainThread, this, location).execute();
    }

    @Override
    public void onCachedDataError() {
        view.hideLoading();
        view.showError("Error reading cached data");
    }

    @Override
    public void onCachedDataRead(List<BarData> entries, long timeStamp) {

    }

    @Override
    public void onDataCached(List<BarData> lines) {

    }

    @Override
    public void onLiquiditySidesFiltered(List<LiquiditySide> data) {
        view.hideLoading();

        if (priceMin > 0 || priceMax > 0) {
            new LiquiditySidePriceInteractorImpl(executor, mainThread, this, data, priceMin, priceMax).execute();
        } else {
            view.onLiquiditySidesGenerated(data);
        }
    }

    @Override
    public void onLiquiditySideForPriceFound(List<LiquiditySide> data) {
        view.hideLoading();
        view.onLiquiditySidesGenerated(data);
    }
}
