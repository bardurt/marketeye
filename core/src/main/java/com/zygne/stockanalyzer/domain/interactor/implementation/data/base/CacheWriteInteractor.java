package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.BarData;

import java.util.List;

public interface CacheWriteInteractor extends Interactor {

    interface Callback{
        void onDataCached(List<BarData> lines);
    }
}

