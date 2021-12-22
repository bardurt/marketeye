package com.zygne.stockanalyzer.domain.interactor.implementation.data.io;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.CacheReadInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CacheReadInteractorImpl extends BaseInteractor implements CacheReadInteractor {

    private final Callback callback;
    private final String filePath;

    public CacheReadInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String filePath) {
        super(executor, mainThread);
        this.callback = callback;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        List<BarData> data = new ArrayList<>();

        File file;
        FileReader fileReader = null;

        try {
            file = new File(filePath);
            fileReader = new FileReader(file);
        } catch (Exception ignored) {
        }

        long timeStamp = System.currentTimeMillis();
        if (fileReader != null) {
            try {
                BufferedReader br = new BufferedReader(fileReader);
                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    if (count == 0) {
                        try {
                            timeStamp = Long.parseLong(line);
                        } catch (Exception ignored) {
                        }

                    }

                    if (count > 1) {
                        BarData barData = BarData.fromStream(line);

                        if (barData != null) {
                            data.add(barData);
                        }
                    }
                    count++;
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final long time = timeStamp;

        mainThread.post(() -> callback.onCachedDataRead(data, time));
    }
}
