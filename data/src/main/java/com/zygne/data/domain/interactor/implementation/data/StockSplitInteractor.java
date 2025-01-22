package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.interactor.Interactor;
import com.zygne.data.domain.model.Histogram;

import java.util.List;

public interface StockSplitInteractor extends Interactor {

    interface Callback {
        void onStockSplitsDetected(List<Histogram> data);
    }
}
