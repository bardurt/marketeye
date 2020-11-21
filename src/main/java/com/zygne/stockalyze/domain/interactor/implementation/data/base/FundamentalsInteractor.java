package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Fundamentals;

public interface FundamentalsInteractor extends Interactor {

    interface Callback{
        void onFundamentalsFetched(Fundamentals fundamentals);
    }
}
