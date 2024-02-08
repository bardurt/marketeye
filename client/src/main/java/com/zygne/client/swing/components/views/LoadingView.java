package com.zygne.client.swing.components.views;

import com.zygne.client.swing.StringUtils;

import javax.swing.*;
import java.awt.*;

public class LoadingView extends JPanel {

    private JLabel jLabel;

    private Thread thread;
    private LoadingRunnable loadingRunnable;

    public LoadingView() {
        setLayout(new BorderLayout());
        jLabel = new JLabel();
        add(jLabel, BorderLayout.CENTER);
    }

    public void showLoading(String message) {

        if(loadingRunnable != null){
            loadingRunnable.stop();
            loadingRunnable = null;
        }

        loadingRunnable = new LoadingRunnable(message, jLabel);

        Thread t = new Thread(loadingRunnable);
        t.start();
    }

    public void hideLoading() {

        if (loadingRunnable != null) {
            loadingRunnable.stop();
        }
    }


    private class LoadingRunnable implements Runnable {

        private static final long TIME_TO_SLEEP = 1000;

        private int MAX_DOTS = 3;

        private String content;
        private JLabel jLabel;
        private volatile boolean running;

        public LoadingRunnable(String content, JLabel jLabel) {
            this.content = content;
            this.jLabel = jLabel;
        }

        @Override
        public void run() {

            int count = 0;
            running = true;
            while (running) {
                try {

                    count = ++count % MAX_DOTS;

                    String loadingString = content+ StringUtils.repeatAndPad(".", count+1, MAX_DOTS);
                    jLabel.setText(loadingString);
                    Thread.sleep(TIME_TO_SLEEP);
                } catch (InterruptedException e) {
                    running = false;
                }
            }

            jLabel.setText("");
        }

        public synchronized void stop() {
            running = false;
        }
    }
}
