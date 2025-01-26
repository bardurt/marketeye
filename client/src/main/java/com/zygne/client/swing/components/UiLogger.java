package com.zygne.client.swing.components;

import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.MainThread;

import javax.swing.JLabel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class UiLogger extends Thread implements Logger {

    private final BlockingQueue<Command> itemsToLog = new ArrayBlockingQueue<>(100);
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final MainThread mainThread;
    private final JLabel outPut;

    private volatile boolean started = false;
    private volatile boolean shuttingDown = false;

    public UiLogger(MainThread mainThread, JLabel outPut) {
        this.mainThread = mainThread;
        this.outPut = outPut;
    }

    @Override
    public void shutDown() {
        shuttingDown = true;
        try {
            itemsToLog.put(new Command.ShutDown());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUp() {
        if (!started) {
            started = true;
            this.setDaemon(true);
            this.start();
        }
    }

    @Override
    public void log(LOG_LEVEL level, String message) {
        if (started && !shuttingDown) {
            try {
                itemsToLog.put(new Logger.Command.Log(level, message));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void clear() {
        if (started && !shuttingDown) {
            try {
                itemsToLog.clear();
                itemsToLog.put(new Logger.Command.Clear());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        try {

            var running = true;
            while (running) {
                Command item = itemsToLog.take();
                mainThread.post(() -> {
                    if (outPut != null) {
                        if (item instanceof Command.Log) {
                            String log = "\n" + getTime() + " - " + ((Command.Log) item).getMessage();
                            mainThread.post(() -> outPut.setText(log));

                        } else if (item instanceof Command.Clear) {
                            mainThread.post(() -> outPut.setText(""));
                        }
                    }
                });
                if (shuttingDown) {
                    running = false;
                }
            }
        } catch (InterruptedException ignored) {
        } finally {
            started = false;
        }
    }

    private String getTime() {
        return formatter.format(new Date());
    }
}
