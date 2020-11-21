package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.util.List;

public interface DataMergeInteractor extends Interactor {

    interface Callback {
        void onDataMerged(List<String> entries);
    }
}
