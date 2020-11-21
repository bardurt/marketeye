package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.util.List;

public interface CacheWriteInteractor extends Interactor {

    interface Callback{
        void onDataCached(List<String> lines);
    }
}

