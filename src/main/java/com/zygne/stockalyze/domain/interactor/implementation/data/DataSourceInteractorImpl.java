package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataSourceInteractor;

public class DataSourceInteractorImpl extends BaseInteractor implements DataSourceInteractor {

    private final Callback callback;
    private final String source;

    public DataSourceInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String source) {
        super(executor, mainThread);
        this.callback = callback;
        this.source = source;
    }

    @Override
    public void run() {
        if (source.contains(".csv")) {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.readCsvFile(source);
                }
            });

        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.downloadData(source);
                }
            });

        }
    }
}
