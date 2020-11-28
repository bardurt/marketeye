package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.Histogram;

import java.util.List;

public interface HistogramInteractor extends Interactor {

    interface Callback{
        void onHistogramCreated(List<Histogram> data);
    }
}
