package com.zygne.stockanalyzer.presentation.presenter.implementation;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.BasePresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.InteractiveBrokersDelegate;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.alphavantage.AlphaVantageDelegate;

public class MainPresenterImpl extends BasePresenter implements MainPresenter {

    private final View view;
    private final MainPresenter delegate;

    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view, Settings settings) {
        super(executor, mainThread);
        this.view = view;
        this.mainThread = mainThread;
        if(settings.getDataProvider() == DataProvider.INTERACTIVE_BROKERS) {
            delegate = new InteractiveBrokersDelegate(executor, mainThread, view, settings);
        } else {
            delegate = new AlphaVantageDelegate(executor, mainThread, view, settings);
        }
    }

    @Override
    public void getZones(String ticker, double percentile, TimeInterval timeInterval, int monthsToFetch, boolean fundamentalData) {
        delegate.getZones(ticker, percentile, timeInterval, monthsToFetch, fundamentalData);
    }

    @Override
    public void toggleConnection() {
        delegate.toggleConnection();
    }

}
