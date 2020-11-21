package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.PowerZone;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;
import com.zygne.stockalyze.presentation.view.BaseView;

import java.util.List;

public interface LiquiditySidePresenter {

    void getSides(String ticker, TimeFrame timeFrame, double percentile);

    interface View extends BaseView {
        void onLiquiditySidesGenerated(List<PowerZone> data);
    }
}
