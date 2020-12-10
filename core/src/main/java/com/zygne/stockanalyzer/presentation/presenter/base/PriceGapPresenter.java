package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface PriceGapPresenter {

    public void findGaps(List<Histogram> histogramLiist);

    interface View extends BaseView {
        void onPriceGapsFound(List<PriceGap> data);
    }
}
