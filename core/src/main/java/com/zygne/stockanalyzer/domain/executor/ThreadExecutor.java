package com.zygne.stockanalyzer.domain.executor;


import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor implements Executor {
    private static final ThreadExecutor ourInstance = new ThreadExecutor();

    public static ThreadExecutor getInstance() {
        return ourInstance;
    }

    private ThreadExecutor() {
    }

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final long TIME_TO_LIVE = 60L;
    private final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<>();

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            TIME_TO_LIVE,
            TimeUnit.SECONDS,
            WORK_QUEUE
    );

    @Override
    public void execute(BaseInteractor interactor) {
        threadPoolExecutor.submit(interactor::run);
    }

    @Override
    public void stop() {
        threadPoolExecutor.shutdownNow();
    }
}
