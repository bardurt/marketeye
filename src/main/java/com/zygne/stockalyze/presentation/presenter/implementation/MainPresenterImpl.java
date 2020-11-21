package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.model.Settings;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;
import com.zygne.stockalyze.presentation.presenter.base.BasePresenter;
import com.zygne.stockalyze.presentation.presenter.base.MainPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.delegates.alphavantage.AlphaVantageDelegate;

public class MainPresenterImpl extends BasePresenter implements MainPresenter {

    private final View view;
    private final MainPresenter delegate;

    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view, Settings settings) {
        super(executor, mainThread);
        this.view = view;
        this.mainThread = mainThread;
        delegate = new AlphaVantageDelegate(executor, mainThread, view, settings);
    }

    @Override
    public void getZones(String ticker, double percentile, TimeFrame timeFrame, boolean fundamentalData) {
        if (ticker.isEmpty()) {
            view.showError("No Ticker name!");
            return;
        }

        delegate.getZones(ticker, percentile, timeFrame, fundamentalData);
    }


}
