package com.zygne.data.presentation.presenter.implementation;

import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;
import com.zygne.data.CotDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.interactor.implementation.data.CotInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.base.CotInteractor;
import com.zygne.data.domain.model.CotData;
import com.zygne.data.presentation.presenter.base.CotPresenter;

import java.util.List;

public class CotPresenterImpl extends BasePresenter implements CotPresenter, CotInteractor.Callback {

    private final DataBroker dataBroker;
    private final View view;

    public CotPresenterImpl(Executor executor, MainThread mainThread, View view, Logger logger) {
        super(executor, mainThread);
        this.dataBroker = new CotDataBroker(logger);
        this.view = view;
    }

    @Override
    public void createReport(String symbol) {
        CotInteractor cotInteractor = new CotInteractorImpl(executor, mainThread, this, symbol, dataBroker);
        cotInteractor.execute();
    }

    @Override
    public void onCotDataLoaded(List<CotData> entries) {
        view.onCotDataReady(entries);
    }

    @Override
    public void onError() {

    }
}
