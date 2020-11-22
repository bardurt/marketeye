package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.LiquidityLevel;

import java.util.List;

public interface LiquidityLevelInteractor extends Interactor {

    interface Callback{
        void onLiquidityLevelsCreated(List<LiquidityLevel> data);
    }
}
