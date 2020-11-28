package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.CacheWriteInteractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CacheWriteInteractorImpl extends BaseInteractor implements CacheWriteInteractor {

    private final Callback callback;
    private final String folder;
    private final String ticker;
    private final List<String> lines;

    public CacheWriteInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String folder, String ticker, List<String> lines) {
        super(executor, mainThread);
        this.callback = callback;
        this.folder = folder;
        this.ticker = ticker;
        this.lines = lines;
    }

    @Override
    public void run() {

        String root = folder;

        File folder = new File(root);

        String timeStamp = "" + System.currentTimeMillis();

        folder.mkdirs();

        String fileName = folder.getAbsolutePath() + "/" + ticker.toUpperCase() +".csv";

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.append(timeStamp);
            myWriter.append("\n");
            for(String line : lines){
                myWriter.append(line);
                myWriter.append("\n");
            }
            myWriter.close();
        } catch (IOException ignored) { }

        mainThread.post(() -> callback.onDataCached(lines));

    }
}
