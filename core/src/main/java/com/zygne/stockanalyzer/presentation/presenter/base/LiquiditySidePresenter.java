package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface LiquiditySidePresenter {

    void getSides(String ticker,
                  TimeInterval timeInterval,
                  double size,
                  double percentile,
                  double priceMin,
                  double priceMax);

    interface View extends BaseView {
        void onLiquiditySidesGenerated(List<LiquiditySide> data);
    }
}
