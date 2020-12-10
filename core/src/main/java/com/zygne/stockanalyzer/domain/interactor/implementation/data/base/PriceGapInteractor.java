package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.PriceGap;

import java.util.List;

public interface PriceGapInteractor extends Interactor {

    interface Callback{
        void onPriceGapsFound(List<PriceGap> data);
    }
}
