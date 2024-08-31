package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.Ratio;

public interface RatioInteractor {

    interface Callback{
        void onRatioCreated(Ratio ratio);
    }
}
