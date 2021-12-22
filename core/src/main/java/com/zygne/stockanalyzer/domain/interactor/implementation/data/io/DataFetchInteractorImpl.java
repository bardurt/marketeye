package com.zygne.stockanalyzer.domain.interactor.implementation.data.io;

import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class DataFetchInteractorImpl extends BaseInteractor implements DataFetchInteractor, DataBroker.Callback  {

    private final Callback callback;
    private final String symbol;
    private final TimeInterval timeInterval;
    private final DataSize length;
    private final DataBroker dataBroker;
    private final List<BarData> data;

    public DataFetchInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String symbol, TimeInterval timeInterval, DataSize length, DataBroker dataBroker) {
        super(executor, mainThread);
        this.callback = callback;
        this.symbol = symbol;
        this.timeInterval = timeInterval;
        this.length = length;
        this.dataBroker = dataBroker;
        data = new ArrayList<>();
    }

    @Override
    public void run() {
        dataBroker.setCallback(this);
        dataBroker.downloadHistoricalBarData(symbol, length, timeInterval);
    }

    @Override
    public void onDataFinished(List<BarData> data) {
        dataBroker.removeCallback();
        if(data.isEmpty()){
            mainThread.post(() -> callback.onDataFetchError("No date found for " + symbol));
            return;
        }
        this.data.addAll(data);
        String timeStamp = data.get(data.size()-1).getTime();
        mainThread.post(() -> callback.onDataFetched(data, timeStamp));
    }

    @Override
    public void onTickPriceFetched(double price) {

    }

}
