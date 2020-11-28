package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

import java.util.List;

public interface CacheReadInteractor extends Interactor {

    interface Callback{
        void onCachedDataRead(List<String> entries, long timeStamp);
    }
}
