package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.GapResult;
import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public interface GapAnalysisPresenter  {

    public void analyseGaps(String ticker);

    public GapResult getGapResult();

    interface View extends BaseView{
        void onGapAnalysisFinished(String details);
    }
}
