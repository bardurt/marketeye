package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.util.List;

public interface CacheReadInteractor extends Interactor {

    interface Callback{
        void onCachedDataRead(List<String> entries, long timeStamp);
    }
}
