package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.Histogram;

import java.util.List;

public interface WeeklyHistogramInteractor {

    interface Callback{
        void onWeeklyHistogramCreated(List<Histogram> data);
    }
}
