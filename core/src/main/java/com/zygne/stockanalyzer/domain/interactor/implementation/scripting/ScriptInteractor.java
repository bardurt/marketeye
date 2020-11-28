package com.zygne.stockanalyzer.domain.interactor.implementation.scripting;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

public interface ScriptInteractor extends Interactor {

    interface Callback{
        void onScriptCreated(String script, String name);
    }
}
