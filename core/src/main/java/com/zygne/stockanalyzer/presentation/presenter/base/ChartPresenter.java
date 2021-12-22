package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface ChartPresenter {

    void setSupply(List<LiquidityLevel> liquidityLevels);

    void setAsset(DataBroker.Asset asset);

    void getChartData(String ticker, TimeInterval timeInterval, DataSize dataSize);

    void setHistograms(List<Histogram> histograms);

    interface View extends BaseView {
        void onChartReady(List<Histogram> histograms,
                          List<PriceGap> priceGaps,
                          List<PriceImbalance> priceImbalances,
                          List<LiquidityLevel> liquidityLevels);
    }
}
