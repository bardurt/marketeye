package com.zygne.stockalyze.domain;

public interface MainThread {

    void post(Runnable runnable);
}
