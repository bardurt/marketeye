package client;


import com.zygne.stockanalyzer.domain.executor.MainThread;
import javafx.application.Platform;

public class JavaFxThread implements MainThread {
    @Override
    public void post(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
