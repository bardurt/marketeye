package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.Fundamentals;

public interface FundamentalsInteractor extends Interactor {

    interface Callback{
        void onFundamentalsFetched(Fundamentals fundamentals);
    }
}
