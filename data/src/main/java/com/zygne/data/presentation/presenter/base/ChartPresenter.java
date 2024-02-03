package com.zygne.data.presentation.presenter.base;

import com.zygne.data.domain.model.*;
import com.zygne.arch.presentation.view.BaseView;

import java.util.List;

public interface ChartPresenter {

    void setSupply(List<LiquidityLevel> liquidityLevels);
    void setHistograms(List<Histogram> histograms);

    interface View extends BaseView {
        void onChartReady(List<Histogram> histograms,
                          List<LiquidityLevel> liquidityLevels);
    }
}
