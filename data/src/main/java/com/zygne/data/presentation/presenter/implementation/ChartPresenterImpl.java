package com.zygne.data.presentation.presenter.implementation;

import com.zygne.data.domain.model.*;
import com.zygne.data.presentation.presenter.base.ChartPresenter;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class ChartPresenterImpl extends BasePresenter implements ChartPresenter{

    private View view;

    private List<Histogram> histogramList = new ArrayList<>();

    public ChartPresenterImpl(Executor executor, MainThread mainThread, View view, Logger logger) {
        super(executor, mainThread);
        this.view = view;
    }

    @Override
    public void setHistograms(List<Histogram> histograms) {
        this.histogramList.clear();

        for (Histogram histogram : histograms) {
            histogramList.add(histogram.deepCopy());
        }
    }

    @Override
    public void setSupply(List<LiquidityLevel> liquidityLevels) {
        view.hideLoading();
        view.onChartReady(histogramList, liquidityLevels);
    }
}
