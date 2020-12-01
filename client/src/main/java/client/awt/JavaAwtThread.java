package client.awt;

import com.zygne.stockanalyzer.domain.executor.MainThread;

import javax.swing.*;

public class JavaAwtThread implements MainThread {
    @Override
    public void post(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
