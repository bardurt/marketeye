package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import java.util.List;

public interface PivotInteractor extends Interactor {

    interface Callback{
        void onPivotsFound(List<LiquidityLevel> data);
    }
}
