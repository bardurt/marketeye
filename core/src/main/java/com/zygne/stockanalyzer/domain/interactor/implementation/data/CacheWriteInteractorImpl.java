package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.CacheWriteInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CacheWriteInteractorImpl extends BaseInteractor implements CacheWriteInteractor {

    private final Callback callback;
    private final String folder;
    private final String ticker;
    private final List<BarData> lines;

    public CacheWriteInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String folder, String ticker, List<BarData> lines) {
        super(executor, mainThread);
        this.callback = callback;
        this.folder = folder;
        this.ticker = ticker;
        this.lines = lines;
    }

    @Override
    public void run() {


        lines.sort(new BarData.TimeComparator());
        Collections.reverse(lines);

        String root = folder;

        File folder = new File(root);

        String timeStamp = "" + System.currentTimeMillis();

        folder.mkdirs();

        String fileName = folder.getAbsolutePath() + "/" + ticker.toUpperCase() +".csv";

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.append(timeStamp);
            myWriter.append("\n");
            myWriter.append(BarData.createHeaders());
            myWriter.append("\n");
            for(BarData line : lines){
                myWriter.append(BarData.toStream(line));
                myWriter.append("\n");
            }
            myWriter.close();
        } catch (IOException ignored) { }

        mainThread.post(() -> callback.onDataCached(lines));

    }
}
