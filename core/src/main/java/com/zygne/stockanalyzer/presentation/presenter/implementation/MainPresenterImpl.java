package com.zygne.stockanalyzer.presentation.presenter.implementation;

import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.BasePresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.CryptoCompareDelegate;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.YahooFinanceDelegate;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.AlphaVantageDelegate;

public class MainPresenterImpl extends BasePresenter implements MainPresenter {

    private final MainPresenter delegate;
    private final Logger logger;

    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view, Settings settings, Logger logger) {
        super(executor, mainThread);
        this.mainThread = mainThread;
        this.logger = logger;

        if(settings.getDataProvider() == DataProvider.INTERACTIVE_BROKERS) {
            delegate = new YahooFinanceDelegate(executor, mainThread, view, settings, logger);
        } else if(settings.getDataProvider() == DataProvider.ALPHA_VANTAGE) {
            delegate = new AlphaVantageDelegate(executor, mainThread, view, settings, logger);
        } else if(settings.getDataProvider() == DataProvider.CRYPTO_COMPARE) {
            delegate = new CryptoCompareDelegate(executor, mainThread, view, settings, logger);
        }else {
            delegate = new YahooFinanceDelegate(executor, mainThread, view, settings, logger);
        }
    }

    @Override
    public void createReport(String ticker, double percentile, TimeInterval timeInterval, DataSize dataSize, boolean fundamentalData, boolean cache) {
        ticker = ticker.replaceAll("\\s+", "");
        logger.clear();
        delegate.createReport(ticker, percentile, timeInterval, dataSize, fundamentalData, cache);
    }

    @Override
    public void setAsset(DataBroker.Asset asset) {
        delegate.setAsset(asset);
    }

    @Override
    public void toggleConnection() {
        delegate.toggleConnection();
    }

    @Override
    public void findHighVolume() {
        delegate.findHighVolume();
    }

}
