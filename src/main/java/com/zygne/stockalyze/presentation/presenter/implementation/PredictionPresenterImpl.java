package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.prediction.*;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.*;
import com.zygne.stockalyze.domain.model.GapResult;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;

import java.util.List;

public class PredictionPresenterImpl implements PredictionPresenter,
        NodeInteractor.Callback,
        PredictionInteractor.Callback,
        ChangeInteractor.Callback,
        PointInteractor.Callback,
        DragInteractor.Callback,
        StrongestPullBiasInteractor.Callback,
        NewsBiasInteractor.Callback,
        TrendBiasInteractor.Callback,
        GapBiasInteractor.Callback {

    private View view;
    private List<LiquidityZone> zones;
    private int price;
    private int news = 0;
    private int trend = 0;
    private int gap = 0;
    private GapResult gapResult;

    public PredictionPresenterImpl(View view) {
        this.view = view;
    }


    @Override
    public void startPrediction(List<LiquidityZone> zones, int price, int news, int trend, GapResult gap) {
        this.price = price;
        this.news = news;
        this.trend = trend;
        this.gapResult = gap;

        new NodeInteractorImpl(this, zones, price).execute();
    }

    @Override
    public void onNodesCreated(List<Node> data) {
        new ChangeInteractor(this, data).execute();
    }

    @Override
    public void onPredictionComplete(List<Node> data) {
        view.onPredictionCompleted(data);
    }

    @Override
    public void onStrongestPullBiasCreated(List<Node> data) {
        new NewsBiasInteractor(this, data, news).execute();
    }

    @Override
    public void onDragCreated(List<Node> data) {
        new StrongestPullBiasInteractor(this, data).execute();
    }

    @Override
    public void onPointsCreated(List<Node> data) {
        new DragInteractorImpl(this, data).execute();
    }

    @Override
    public void onChangeCalculated(List<Node> data) {
        new PointInteractorImpl(this, data).execute();
    }

    @Override
    public void onNewsBiasCreated(List<Node> data) {
        new TrendBiasInteractor(this, data, trend).execute();
    }

    @Override
    public void onTrendBiasCreated(List<Node> data) {
        if(this.gapResult != null) {
            new GapBiasInteractorImpl(this, data, gapResult).execute();
        } else {
            new PredictionInteractor(this, data).execute();
        }
    }

    @Override
    public void onGapBiasCreated(List<Node> data) {
        new PredictionInteractor(this, data).execute();
    }
}
