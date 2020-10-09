package com.zygne.stockalyze.domain.interactor.base;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;

public abstract class BaseInteractor implements Interactor {

    protected Executor executor;
    protected MainThread mainThread;

    public BaseInteractor(Executor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }

    protected boolean completed = false;

    public abstract void run();

    @Override
    public void execute() {
        executor.execute(this);
    }
}
