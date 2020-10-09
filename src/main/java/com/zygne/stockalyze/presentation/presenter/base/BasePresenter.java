package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;

public abstract class BasePresenter {

    protected Executor executor;
    protected MainThread mainThread;

    public BasePresenter(Executor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }
}
