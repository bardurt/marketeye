package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface ScriptPresenter {

    void setResistance(List<LiquidityLevel> liquidityLevels);
    void setZones(List<LiquiditySide> zones);
    void setGaps(List<PriceGap> gaps);
    void setSymbol(String symbol);

    void createScript(boolean resistance, boolean zones, boolean gaps);

    interface View extends BaseView {
        void onScriptCreated(String script);
    }
}
