package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.CacheCheckerInteractor;

import java.io.File;

public class CacheCheckerInteractorImpl extends BaseInteractor implements CacheCheckerInteractor {

    private final Callback callback;
    private final String folder;
    private final String ticker;

    public CacheCheckerInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String folder, String ticker) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
        this.folder = folder;
    }

    @Override
    public void run() {

        String path = folder + "/" + ticker.toUpperCase() +".csv";

        File file = new File(path);

        if(file.exists()){
            mainThread.post(() -> callback.onCachedDataFound(path));
        } else {
            mainThread.post(callback::onCachedDataError);
        }

    }
}
