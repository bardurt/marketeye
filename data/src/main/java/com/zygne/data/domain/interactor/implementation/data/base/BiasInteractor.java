package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.Bias;

import java.util.List;

public interface BiasInteractor {

    interface Callback {
        void onBiasCreated(List<Bias> biasList);
    }
}
