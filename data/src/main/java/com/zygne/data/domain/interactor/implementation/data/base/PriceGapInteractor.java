package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.PriceGap;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface PriceGapInteractor extends Interactor {

    interface Callback{
        void onPriceGapsFound(List<PriceGap> data);
    }
}
