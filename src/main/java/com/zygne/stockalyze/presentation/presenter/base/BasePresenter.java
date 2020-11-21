package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;

public abstract class BasePresenter {

    protected final Executor executor;
    protected MainThread mainThread;

    public BasePresenter(Executor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }
}
