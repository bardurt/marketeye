package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.LiquidityLevel;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface LiquidityLevelFilterInteractor extends Interactor {

    interface Callback {
        void onLiquidityLevelsFiltered(List<LiquidityLevel> data);
    }
}
