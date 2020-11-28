package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.enums.TimeFrame;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface LiquiditySidePresenter {

    void getSides(String ticker, TimeFrame timeFrame, double size, double percentile, double price);

    interface View extends BaseView {
        void onLiquiditySidesGenerated(List<LiquiditySide> data);
    }
}
