package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.GapResult;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.presentation.presenter.base.BaseView;

import java.util.List;

public interface PredictionPresenter {

    void startPrediction(List<LiquidityZone> zones, int price, int news, int trend, GapResult gap);

    interface View extends BaseView {
        void onPredictionCompleted(List<Node> nodes);
    }
}
