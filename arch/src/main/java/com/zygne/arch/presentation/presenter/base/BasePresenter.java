package com.zygne.arch.presentation.presenter.base;

import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;

public abstract class BasePresenter {

    protected final Executor executor;
    protected MainThread mainThread;

    public BasePresenter(Executor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }
}
