package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.flow;

import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.DataFetchInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.HistogramInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.VolumeBarDetailsInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.VolumeBarDetailsInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.VolumeBarDetails;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.util.List;

public class DailyVolumeFlow implements
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        VolumeBarDetailsInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private final DataBroker dataBroker;
    private List<Histogram> histogramList;

    public DailyVolumeFlow(Executor executor, MainThread mainThread, Callback callback, DataBroker dataBroker) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
        this.dataBroker = dataBroker;
    }

    public void findVolume(String ticker) {
        new DataFetchInteractorImpl(executor, mainThread, this, ticker, TimeInterval.Day, new DataSize(3, DataSize.Unit.Year), dataBroker).execute();
    }

    @Override
    public void onDataFetched(List<BarData> entries, String timestamp) {
        new HistogramInteractorImpl(executor, mainThread, this, entries).execute();
    }

    @Override
    public void onDataFetchError(String message) {
    }

    @Override
    public void onStatusUpdate(String message) {
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        this.histogramList = data;
        new VolumeBarDetailsInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onVolumeBarDetailsCreated(List<VolumeBarDetails> data) {
        callback.onDailyHighVolumeFound(data, histogramList);
    }

    public interface Callback {
        void onDailyHighVolumeFound(List<VolumeBarDetails> data, List<Histogram> histograms);
    }
}
