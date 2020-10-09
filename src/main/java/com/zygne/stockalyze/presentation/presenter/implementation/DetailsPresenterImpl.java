package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.data.YahooFloatInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.YahooMarketPriceInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.MarketPriceInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.StockFloatInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.presentation.presenter.base.BasePresenter;
import com.zygne.stockalyze.presentation.presenter.base.DetailsPresenter;

import java.util.List;

public class DetailsPresenterImpl extends BasePresenter implements DetailsPresenter,
        StockFloatInteractor.Callback,
        MarketPriceInteractor.Callback{

    private View view;
    private String ticker;
    private String details = "";
    private List<Histogram> histogramList;

    public DetailsPresenterImpl(Executor executor, MainThread mainThread, View view) {
        super(executor, mainThread);
        this.view = view;
    }


    @Override
    public void fetchDetails(String ticker, List<Histogram> histogramList) {
        this.ticker = ticker;
        this.histogramList = histogramList;
        view.showLoading("Fetching details for " + ticker.toUpperCase());
        details = "";
        details = "Details for : " + ticker.toUpperCase();
        details += "\n" + histogramList.size() + " bars";

        new YahooFloatInteractor(executor, mainThread, this, ticker).execute();
    }

    @Override
    public void onStockFloatFetched(int stockFloat) {
        details += "\nStock float : ";
        if (stockFloat < 0) {
            details += " - ";
        } else {
            details += String.format("%,d", stockFloat);
        }

        new YahooMarketPriceInteractor(executor, mainThread, this, ticker).execute();
    }

    @Override
    public void onMarketPriceFetched(double price) {

        details += "\n";
        details += "Market Price : ";
        if (price < 0) {
            details += " - ";
        } else {
            details += String.format("%.2f", price);
        }

        view.hideLoading();
        view.onDetailsFetched(details);
    }

}
