package com.zygne.data.presentation.presenter.base;

import com.zygne.data.domain.model.*;
import com.zygne.data.domain.model.enums.TimeInterval;
import com.zygne.arch.presentation.view.BaseView;

import java.util.List;

public interface ChartPresenter {

    void setSupply(List<LiquidityLevel> liquidityLevels);

    void getChartData(String ticker, TimeInterval timeInterval, DataSize dataSize);

    void setHistograms(List<Histogram> histograms);

    interface View extends BaseView {
        void onChartReady(List<Histogram> histograms,
                          List<PriceGap> priceGaps,
                          List<PriceImbalance> priceImbalances,
                          List<LiquidityLevel> liquidityLevels);
    }
}
