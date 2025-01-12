package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.data.domain.model.Histogram;

import java.util.List;

public interface MonthlyHistogramInteractor {

    interface Callback{
        void onMonthlyHistogramCreated(List<Histogram> data);
    }
}
