package com.zygne.stockanalyzer.domain.interactor.base;


import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;

public abstract class BaseInteractor implements Interactor {

    protected final Executor executor;
    protected final MainThread mainThread;

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
