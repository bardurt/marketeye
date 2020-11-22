package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.LiquidityLevel;

import java.util.List;

public interface LiquidityLevelFilterInteractor extends Interactor {

    interface Callback {
        void onLiquidityLevelsFiltered(List<LiquidityLevel> data);
    }
}
