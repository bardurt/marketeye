package com.zygne.stockalyze.ui.fx;

import com.zygne.stockalyze.domain.MainThread;
import javafx.application.Platform;

public class JavaFxThread implements MainThread {
    @Override
    public void post(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
