package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockalyze.domain.utils.StringHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderInteractor extends BaseInteractor implements DataFetchInteractor {

    private final Callback callback;
    private final String filePath;

    public CsvReaderInteractor(Executor executor, MainThread mainThread, Callback callback, String filePath) {
        super(executor, mainThread);
        this.callback = callback;
        this.filePath = filePath;
    }

    @Override
    public void run() {

        List<String> data = new ArrayList<>();

        File file;
        FileReader fileReader = null;

        try {
            file = new File(filePath);
            fileReader = new FileReader(file);
        } catch (Exception e) {
            callback.onDataFetchError("Could not load data from file : " + filePath );
        }


        if (fileReader != null) {
            try {
                BufferedReader br = new BufferedReader(fileReader);
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onDataFetchError("Could not load data from file : " + filePath );
            }
        }

        mainThread.post(() -> callback.onDataFetched(data, ""));

    }

}
