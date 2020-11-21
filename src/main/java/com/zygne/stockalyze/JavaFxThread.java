package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.executor.MainThread;
import javafx.application.Platform;

public class JavaFxThread implements MainThread {
    @Override
    public void post(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
