package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.data.domain.model.Histogram;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface HistogramInteractor extends Interactor {

    interface Callback{
        void onHistogramCreated(List<Histogram> data);
    }
}
