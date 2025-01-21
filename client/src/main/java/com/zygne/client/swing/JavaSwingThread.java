package com.zygne.client.swing;

import com.zygne.arch.domain.executor.MainThread;

import javax.swing.SwingUtilities;

public class JavaSwingThread implements MainThread {
    @Override
    public void post(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
