package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.PowerZone;
import com.zygne.stockalyze.domain.model.Settings;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;
import com.zygne.stockalyze.presentation.presenter.base.BasePresenter;
import com.zygne.stockalyze.presentation.presenter.base.LiquiditySidePresenter;

import java.util.List;

public class LiquiditySidePresenterImpl extends BasePresenter implements LiquiditySidePresenter,
        CacheCheckerInteractor.Callback,
        CacheWriteInteractor.Callback,
        CacheReadInteractor.Callback,
        HistogramInteractor.Callback,
        PowerZoneInteractor.Callback,
        PowerZoneFilterInteractor.Callback {

    private final View view;
    private final Settings settings;
    private double percentile;

    public LiquiditySidePresenterImpl(Executor executor, MainThread mainThread, View view, Settings settings) {
        super(executor, mainThread);
        this.view = view;
        this.settings = settings;
    }

    @Override
    public void getSides(String ticker, TimeFrame timeFrame, double percentile) {
        this.percentile = percentile;
        view.showLoading("Fetching liquidity sides");
        new CacheCheckerInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeFrame.name()).execute();
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        view.showLoading("Analyzing sides...");
        new PowerZoneInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onPowerZonesCreated(List<PowerZone> data) {
        new PowerZoneFilterInteractorImpl(executor, mainThread, this, data, percentile).execute();
    }

    @Override
    public void onCachedDataRead(List<String> entries, long timeStamp) {
        new HistogramInteractorImpl(executor, mainThread, this, entries).execute();
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
    public void onDataCached(List<String> lines) {
        new HistogramInteractorImpl(executor, mainThread, this, lines).execute();
    }

    @Override
    public void onPowerZonesFiltered(List<PowerZone> data) {
        view.hideLoading();
        view.onLiquiditySidesGenerated(data);
    }
}
