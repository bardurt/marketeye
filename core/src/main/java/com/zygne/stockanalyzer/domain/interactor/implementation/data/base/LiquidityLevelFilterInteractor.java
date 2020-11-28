package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import java.util.List;

public interface LiquidityLevelFilterInteractor extends Interactor {

    interface Callback {
        void onLiquidityLevelsFiltered(List<LiquidityLevel> data);
    }
}
