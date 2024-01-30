package com.zygne.arch.domain.executor;

public interface MainThread {

    void post(Runnable runnable);
}
