package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.MissingDataInteractor;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import java.util.List;

public class MissingDataInteractorImpl extends BaseInteractor implements MissingDataInteractor {

    private final Callback callback;
    private final List<String> entries;

    public MissingDataInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<String> entries) {
        super(executor, mainThread);
        this.callback = callback;
        this.entries = entries;
    }

    @Override
    public void run() {

        String latestEntry = entries.get(0);

        long latestTimestamp = System.currentTimeMillis();

        try {
            String time = latestEntry.split(",",-1)[0];
            latestTimestamp = TimeHelper.getTimeStamp(time);
        } catch (Exception ignored){}

        final int daysMissing = TimeHelper.getDaysDifference(System.currentTimeMillis(), latestTimestamp);
        mainThread.post(() -> callback.onMissingDataCalculated(daysMissing));

    }
}
