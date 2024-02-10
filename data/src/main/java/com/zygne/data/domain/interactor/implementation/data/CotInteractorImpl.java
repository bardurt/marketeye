package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.interactor.implementation.data.base.CotInteractor;
import com.zygne.data.domain.model.CotData;

import java.util.ArrayList;
import java.util.List;

public class CotInteractorImpl  extends BaseInteractor implements CotInteractor, DataBroker.Callback {

    private final Callback callback;
    private final String symbol;
    private final DataBroker dataBroker;
    private final List<CotData> data;

    public CotInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String symbol, DataBroker dataBroker) {
        super(executor, mainThread);
        this.callback = callback;
        this.symbol = symbol;
        this.dataBroker = dataBroker;
        data = new ArrayList<>();
    }

    @Override
    public void run() {
        dataBroker.setCallback(this);

        String url = getCotUrlForSymbol(symbol);

        if(!url.isEmpty()){
            dataBroker.downloadData(url, 0);
        } else {
            mainThread.post(callback::onError);
        }
    }

    @Override
    public void onDataFinished(List<FinanceData> data) {
        dataBroker.removeCallback();

        for (FinanceData d : data) {
            this.data.add((CotData) d);
        }

        mainThread.post(() -> callback.onCotDataLoaded(this.data));
    }

    private String getCotUrlForSymbol(String assetName){

        String cotUrl = switch (assetName) {
            case "%5EGSPC" -> "https://raw.githubusercontent.com/bardurt/cftccot/main/data/cot_sp500.csv";
            case "CL=F" -> "https://raw.githubusercontent.com/bardurt/cftccot/main/data/cot_crude_oil.csv";
            case "%5EIXIC" -> "https://raw.githubusercontent.com/bardurt/cftccot/main/data/cot_nasdaq.csv";
            case "GC=F" -> "https://raw.githubusercontent.com/bardurt/cftccot/main/data/cot_gold.csv";
            case "SI=F" -> "https://raw.githubusercontent.com/bardurt/cftccot/main/data/cot_silver.csv";
            case "KE=F" -> "https://raw.githubusercontent.com/bardurt/cftccot/main/data/cot_wheat.csv";
            default -> "";
        };

        return cotUrl;
    }
}