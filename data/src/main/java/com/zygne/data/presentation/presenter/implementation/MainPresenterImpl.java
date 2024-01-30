package com.zygne.data.presentation.presenter.implementation;

import com.zygne.data.domain.model.DataSize;
import com.zygne.data.domain.model.enums.TimeInterval;
import com.zygne.data.presentation.presenter.base.MainPresenter;
import com.zygne.data.presentation.presenter.implementation.delegates.YahooFinanceDelegate;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;

public class MainPresenterImpl extends BasePresenter implements MainPresenter {

    private final MainPresenter delegate;
    private final Logger logger;

    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view, Logger logger) {
        super(executor, mainThread);
        this.mainThread = mainThread;
        this.logger = logger;

        delegate = new YahooFinanceDelegate(executor, mainThread, view, logger);
    }

    @Override
    public void createReport(String ticker, double percentile, TimeInterval timeInterval, DataSize dataSize) {
        ticker = ticker.replaceAll("\\s+", "");
        logger.clear();
        delegate.createReport(ticker, percentile, timeInterval, dataSize);
    }
}
