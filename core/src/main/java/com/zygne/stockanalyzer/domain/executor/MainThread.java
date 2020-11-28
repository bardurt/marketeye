package com.zygne.stockanalyzer.domain.executor;

public interface MainThread {

    void post(Runnable runnable);
}
