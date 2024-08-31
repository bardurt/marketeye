package com.zygne.data.presentation.presenter.implementation;

import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;
import com.zygne.data.YahooDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.interactor.implementation.data.BiasInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.RatioInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.TendencyInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.base.BiasInteractor;
import com.zygne.data.domain.interactor.implementation.data.base.RatioInteractor;
import com.zygne.data.domain.model.Bias;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.presentation.presenter.base.BiasPresenter;
import com.zygne.data.presentation.presenter.implementation.flow.DataFlow;

import java.util.List;

public class BiasPresenterImpl extends BasePresenter implements
        BiasPresenter,
        DataFlow.Callback,
        BiasInteractor.Callback {

    private final View view;
    private final DataFlow dataFlow;
    private final Logger logger;
    private final DataBroker dataBroker;

    public BiasPresenterImpl(Executor executor, MainThread mainThread, View view, Logger logger) {
        super(executor, mainThread);
        this.view = view;
        this.logger = logger;
        this.dataBroker = new YahooDataBroker(logger);
        this.dataFlow = new DataFlow(executor, mainThread, this, logger);
    }

    @Override
    public void createBias(String symbol) {
        dataFlow.fetchData(dataBroker, symbol, 21, "1mo");
    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        logger.log(Logger.LOG_LEVEL.INFO, "Creating Bias");
        new BiasInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onDataError() {

    }

    @Override
    public void onBiasCreated(List<Bias> biasList) {
        view.onBiasCreated(biasList);
    }
}
