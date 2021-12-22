package com.zygne.stockanalyzer.domain.interactor.implementation.data.io;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.MissingDataInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import java.util.List;

public class MissingDataInteractorImpl extends BaseInteractor implements MissingDataInteractor {

    private final Callback callback;
    private final List<BarData> entries;

    public MissingDataInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<BarData> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
    }

    @Override
    public void run() {

        long latestTimestamp = entries.get(0).getTimeStamp();

        final int daysMissing = TimeHelper.getDaysDifference(System.currentTimeMillis(), latestTimestamp);
        mainThread.post(() -> callback.onMissingDataCalculated(daysMissing));

    }
}
