package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.PowerZone;
import com.zygne.stockalyze.presentation.view.BaseView;

import java.util.List;

public interface ScriptPresenter {

    void setResistance(List<LiquidityZone> liquidityZones);
    void setZones(List<PowerZone> zones);
    void setSymbol(String symbol);

    void createScript(boolean resistance, boolean zones);

    interface View extends BaseView {
        void onScriptCreated(String script);
    }
}
