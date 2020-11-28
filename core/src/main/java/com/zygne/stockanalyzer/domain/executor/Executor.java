package com.zygne.stockanalyzer.domain.executor;

import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;

public interface Executor {

    void execute(BaseInteractor interactor);

    void stop();
}
