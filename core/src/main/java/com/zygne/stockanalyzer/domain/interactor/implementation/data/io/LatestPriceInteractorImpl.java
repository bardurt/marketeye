package com.zygne.stockanalyzer.domain.interactor.implementation.data.io;

import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LatestPriceInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.util.List;

public class LatestPriceInteractorImpl extends BaseInteractor implements LatestPriceInteractor, DataBroker.Callback {


    private final Callback callback;
    private final String symbol;
    private final DataBroker dataBroker;

    public LatestPriceInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String symbol, DataBroker dataBroker) {
        super(executor, mainThread);
        this.callback = callback;
        this.symbol = symbol;
        this.dataBroker = dataBroker;
    }

    @Override
    public void run() {
        dataBroker.setCallback(this);
        dataBroker.getLastTickPrice(symbol);
    }

    @Override
    public void onDataFinished(List<BarData> data) {

    }

    @Override
    public void onTickPriceFetched(double price) {
        mainThread.post(() -> callback.onLatestPriceFetched(price));
    }


}
