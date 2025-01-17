package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.Logger;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.BaseInteractor;

import java.util.ArrayList;
import java.util.List;

public class DataFetchInteractorImpl extends BaseInteractor implements DataFetchInteractor, DataBroker.Callback {

    private final Callback callback;
    private final String symbol;
    private final int years;
    private final DataBroker dataBroker;
    private final List<FinanceData> data;
    private final String timeFrame;
    private final Logger logger;

    public DataFetchInteractorImpl(Executor executor,
                                   MainThread mainThread,
                                   Callback callback,
                                   String symbol,
                                   int yearsToFetch,
                                   String timeFrame,
                                   DataBroker dataBroker,
                                   Logger logger) {
        super(executor, mainThread);
        this.callback = callback;
        this.symbol = symbol;
        this.years = yearsToFetch;
        this.dataBroker = dataBroker;
        this.timeFrame = timeFrame;
        this.logger = logger;
        data = new ArrayList<>();
    }

    @Override
    public void run() {
        dataBroker.setCallback(this);
        dataBroker.downloadData(symbol, timeFrame, years);
    }

    @Override
    public void onDataFinished(List<FinanceData> data) {
        dataBroker.removeCallback();
        if (data.isEmpty()) {
            logger.log(Logger.LOG_LEVEL.INFO, "No data fetched...");
            mainThread.post(() -> callback.onDataFetchError("No data found for " + symbol));
            return;
        }

        this.data.addAll(data);

        mainThread.post(() -> callback.onDataFetched(this.data));
    }
}
