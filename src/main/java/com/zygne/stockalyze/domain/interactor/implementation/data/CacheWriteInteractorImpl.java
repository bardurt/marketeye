package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.CacheWriteInteractor;
import com.zygne.stockalyze.domain.utils.Constants;
import com.zygne.stockalyze.domain.utils.FolderHelper;
import com.zygne.stockalyze.domain.utils.TimeHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CacheWriteInteractorImpl extends BaseInteractor implements CacheWriteInteractor {

    private Callback callback;
    private String ticker;
    private List<String> lines;

    public CacheWriteInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String ticker, List<String> lines) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
        this.lines = lines;
    }

    @Override
    public void run() {

        String root = FolderHelper.getLatestCachedFolder();

        File folder = new File(root);

        folder.mkdirs();

        String fileName = folder.getAbsolutePath() + "/" + ticker.toUpperCase() +".csv";


        try {
            FileWriter myWriter = new FileWriter(fileName);

            for(String line : lines){
                myWriter.append(line);
                myWriter.append("\n");
            }
            myWriter.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }

        mainThread.post(() -> callback.inDataCached(lines));

    }
}
