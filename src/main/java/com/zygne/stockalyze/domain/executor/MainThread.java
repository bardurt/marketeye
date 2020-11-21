package com.zygne.stockalyze.domain.executor;

public interface MainThread {

    void post(Runnable runnable);
}
