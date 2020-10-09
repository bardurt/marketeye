package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.CacheCheckerInteractor;
import com.zygne.stockalyze.domain.utils.FolderHelper;

import java.io.File;

public class CacheCheckerInteractorImpl extends BaseInteractor implements CacheCheckerInteractor {

    private Callback callback;
    private String ticker;

    public CacheCheckerInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String ticker) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
    }

    @Override
    public void run() {

        String path = FolderHelper.getLatestCachedFolder() + "/" + ticker.toUpperCase() +".csv";

        File file = new File(path);

        if(file.exists()){
            mainThread.post(() -> callback.onCachedDataFound(path));
        } else {
            mainThread.post(() -> callback.onCachedDataError());
        }

    }
}
