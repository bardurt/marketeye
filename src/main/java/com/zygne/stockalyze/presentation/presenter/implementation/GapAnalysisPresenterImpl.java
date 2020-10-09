package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.data.GapRateInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.data.HistogramInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.data.YahooDataInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.GapRateInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockalyze.domain.model.GapResult;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.presentation.presenter.base.BasePresenter;
import com.zygne.stockalyze.presentation.presenter.base.GapAnalysisPresenter;

import java.util.List;

public class GapAnalysisPresenterImpl extends BasePresenter implements
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        GapAnalysisPresenter,
        GapRateInteractor.Callback {

    private View view;
    private GapResult gapResult = null;
    private double limit = 10;

    public GapAnalysisPresenterImpl(Executor executor, MainThread mainThread, View view) {
        super(executor, mainThread);
        this.view = view;
    }

    @Override
    public void analyseGaps(String ticker) {
        view.showLoading("Analyzing gaps...");
        new YahooDataInteractor(executor, mainThread, this, ticker).execute();
    }

    @Override
    public void onGapRateCalculated(GapResult gapResult) {
        view.hideLoading();
        this.gapResult = gapResult;

        String details = "";
        details += "Gap Floor " + String.format("%.2f", gapResult.gapLimit);
        details += "\n";
        details += "Total Gaps : " + gapResult.gapCount;
        details += "\n";
        details += "Bullish Gaps : " + gapResult.gapBull;
        details += "\n";
        details += "\n";
        details += "O/C High Range : " + String.format("%.2f", gapResult.openCloseRange.max) + "%";
        details += "\n";
        details += "O/C Low Range : " + String.format("%.2f", gapResult.openCloseRange.min) + "%";
        details += "\n";
        details += "O/C Avg Range : " + String.format("%.2f", gapResult.openCloseRange.avg) + "%";
        details += "\n";
        details += "\n";
        details += "L/H High Range : " + String.format("%.2f", gapResult.lowHighRange.max) + "%";
        details += "\n";
        details += "L/H Low Range : " + String.format("%.2f", gapResult.lowHighRange.min) + "%";
        details += "\n";
        details += "L/H Avg Range : " + String.format("%.2f", gapResult.lowHighRange.avg) + "%";
        details += "\n";
        details += "\n";
        details += "O/H Avg Range : " + String.format("%.2f", gapResult.openHighRange.avg) + "%";

        view.hideLoading();
        view.onGapAnalysisFinished(details);
    }

    @Override
    public GapResult getGapResult() {
        return gapResult;
    }

    @Override
    public void onDataFetched(List<String> entries, String ticker) {
        view.showLoading("Preparing data...");
        new HistogramInteractorImpl(executor, mainThread, this, entries).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        view.showLoading("Fetching data...");
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        view.showLoading("Analyzing gaps...");
        new GapRateInteractorImpl(executor, mainThread, this, data, limit).execute();
    }
}
