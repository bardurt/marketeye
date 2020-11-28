package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import java.util.List;

public interface LiquidityLevelInteractor extends Interactor {

    interface Callback{
        void onLiquidityLevelsCreated(List<LiquidityLevel> data);
    }
}
