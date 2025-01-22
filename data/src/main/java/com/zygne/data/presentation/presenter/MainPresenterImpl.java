package com.zygne.data.presentation.presenter;

import com.zygne.data.AlphaVantageDataBroker;
import com.zygne.data.CryptoDataBroker;
import com.zygne.data.PolygonDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.interactor.implementation.data.DataFetchInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.HistogramInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.MonthlyHistogramInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.StockSplitInteractor;
import com.zygne.data.domain.interactor.implementation.data.StockSplitInteractorImpl;
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
        StockSplitInteractor.Callback,
        WeeklyHistogramInteractor.Callback,
        MonthlyHistogramInteractor.Callback,
        TendencyInteractor.Callback {

    private final DataBroker dataBroker;
    private final DataBroker cryptoBroker;
    private final DataBroker polygonBroker;
    private final View view;
    private String ticker;
    private boolean downloadingData = false;
    private final List<BarData> downloadedData = new ArrayList<>();
    private final List<Histogram> rawHistograms = new ArrayList<>();
    private final List<Histogram> histogramDaily = new ArrayList<>();
    private final List<Histogram> histogramWeekly = new ArrayList<>();
    private final List<Histogram> histogramMonthly = new ArrayList<>();
    private final Logger logger;
    private boolean canAdjust = false;

    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view, Logger logger, String avApi, String polygonApi) {
        super(executor, mainThread);
        this.mainThread = mainThread;
        this.dataBroker = new AlphaVantageDataBroker(logger, avApi);
        this.cryptoBroker = new CryptoDataBroker(logger);
        this.polygonBroker = new PolygonDataBroker(logger, polygonApi);
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
            canAdjust = true;
            new DataFetchInteractorImpl(executor,
                    mainThread,
                    this,
                    ticker,
                    5,
                    "",
                    dataBroker,
                    logger).execute();
        } else if (type == 1) {
            canAdjust = false;
            new DataFetchInteractorImpl(executor,
                    mainThread,
                    this,
                    ticker,
                    5,
                    "",
                    polygonBroker,
                    logger).execute();
        } else if (type == 2) {
            canAdjust = false;
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
    public void adjust(boolean adjustPrices) {
        if (!canAdjust) {
            logger.log(Logger.LOG_LEVEL.INFO, "Cannot adjust prices");
            return;
        }

        if (rawHistograms.isEmpty()) {
            view.showError("No data to adjust");
            return;
        }

        if (adjustPrices) {
            new StockSplitInteractorImpl(executor, mainThread, this, logger, rawHistograms).execute();
        } else {
            histogramDaily.clear();
            histogramDaily.addAll(rawHistograms);
            new WeeklyHistogramInteractorImpl(executor, mainThread, this, rawHistograms).execute();
        }
    }


    @Override
    public void onDataFetched(List<FinanceData> entries) {
        downloadedData.clear();
        for (FinanceData e : entries) {
            downloadedData.add((BarData) e);
        }
        new HistogramInteractorImpl(executor, mainThread, this, logger, downloadedData).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        view.hideLoading();

        downloadingData = false;
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        logger.log(Logger.LOG_LEVEL.INFO, "Histogram fetch");
        logger.log(Logger.LOG_LEVEL.INFO, "Daily Bars Created");
        rawHistograms.clear();
        rawHistograms.addAll(data);
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

        try {
            new TendencyInteractorImpl(executor, mainThread, this, histogramDaily).execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTendencyReportCreated(TendencyReport tendencyReport) {
        logger.log(Logger.LOG_LEVEL.INFO, "Tendency Created");
        downloadingData = false;
        view.hideLoading();
        view.onComplete(histogramDaily, histogramWeekly, histogramMonthly, tendencyReport, ticker);
    }

    @Override
    public void onStockSplitsDetected(List<Histogram> data) {
        logger.log(Logger.LOG_LEVEL.INFO, "Daily Bars Created");
        histogramDaily.clear();
        histogramDaily.addAll(data);
        new WeeklyHistogramInteractorImpl(executor, mainThread, this, data).execute();
    }
}
