package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.presentation.presenter.base.BaseView;

import java.util.List;

public interface MainPresenter {

    void filterZones(double topPercent);
    void getZones(String ticker, int source, int vpaRule);

    List<Histogram> getHistogram();

    public interface View extends BaseView {
        void onZonesCreated(List<LiquidityZone> zones);
        void onZonesFiltered(List<LiquidityZone> zones);
    }
}
