package com.zygne.data.presentation.presenter;

import com.zygne.data.AlphaVantageDataBroker;
import com.zygne.data.CryptoDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.interactor.implementation.data.DataFetchInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.HistogramInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.MonthlyHistogramInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.TendencyInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.WeeklyHistogramInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.DataFetchInteractor;
import com.zygne.data.domain.interactor.implementation.data.HistogramInteractor;
import com.zygne.data.domain.interactor.implementation.data.MonthlyHistogramInteractor;
import com.zygne.data.domain.interactor.implementation.data.TendencyInteractor;
import com.zygne.data.domain.interactor.implementation.data.WeeklyHistogramInteractor;
import com.zygne.data.domain.model.BarData;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.TendencyReport;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class MainPresenterImpl extends BasePresenter implements MainPresenter,
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        WeeklyHistogramInteractor.Callback,
        MonthlyHistogramInteractor.Callback,
        TendencyInteractor.Callback {

    private final DataBroker dataBroker;
    private final DataBroker cryptoBroker;
    private final View view;
    private String ticker;
    private boolean downloadingData = false;
    private final List<BarData> downloadedData = new ArrayList<>();
    private final List<Histogram> histogramDaily = new ArrayList<>();
    private final List<Histogram> histogramWeekly = new ArrayList<>();
    private final List<Histogram> histogramMonthly = new ArrayList<>();
    private final Logger logger;

    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view, Logger logger, String apiKey) {
        super(executor, mainThread);
        this.mainThread = mainThread;
        this.dataBroker = new AlphaVantageDataBroker(logger, apiKey);
        this.cryptoBroker = new CryptoDataBroker(logger);
        this.view = view;
        this.view.showError("");
        this.logger = logger;

        view.prepareView();
    }

    @Override
    public void createReport(String ticker, int type) {

        if (downloadingData) {
            return;
        }

        if (ticker.isEmpty()) {
            view.showError("No Symbol name!");
            return;
        }

        downloadingData = true;
        this.ticker = ticker.replaceAll("\\s+", "");

        view.showError("");

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        if (type == 0) {
            new DataFetchInteractorImpl(executor,
                    mainThread,
                    this,
                    ticker,
                    5,
                    "",
                    dataBroker,
                    logger).execute();
        } else {
            new DataFetchInteractorImpl(executor,
                    mainThread,
                    this,
                    ticker,
                    5,
                    "",
                    cryptoBroker,
                    logger).execute();
        }
    }


    @Override
    public void onDataFetched(List<FinanceData> entries) {
        downloadedData.clear();
        for (FinanceData e : entries) {
            downloadedData.add((BarData) e);
        }
        new HistogramInteractorImpl(executor, mainThread, this, downloadedData).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        view.hideLoading();
        view.showError("Unable to fetch data");
        downloadingData = false;
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        logger.log(Logger.LOG_LEVEL.INFO, "Daily Bars Created");
        histogramDaily.clear();
        histogramDaily.addAll(data);
        new WeeklyHistogramInteractorImpl(executor, mainThread, this, data).execute();
    }


    @Override
    public void onWeeklyHistogramCreated(List<Histogram> data) {
        logger.log(Logger.LOG_LEVEL.INFO, "Weekly Bars Created");
        histogramWeekly.clear();
        histogramWeekly.addAll(data);
        new MonthlyHistogramInteractorImpl(executor, mainThread, this, histogramDaily).execute();
    }

    @Override
    public void onMonthlyHistogramCreated(List<Histogram> data) {
        logger.log(Logger.LOG_LEVEL.INFO, "Monthly Bars Created");
        histogramMonthly.clear();
        histogramMonthly.addAll(data);
        new TendencyInteractorImpl(executor, mainThread, this, histogramDaily).execute();
    }

    @Override
    public void onTendencyReportCreated(TendencyReport tendencyReport) {
        logger.log(Logger.LOG_LEVEL.INFO, "Tendency Created");
        downloadingData = false;
        view.hideLoading();
        view.onComplete(histogramDaily, histogramWeekly, histogramMonthly, tendencyReport, ticker);
    }
}
