package com.zygne.stockanalyzer.domain.api;

public interface Api {

    void connect();
    void disconnect();

    void setConnectionListener(ConnectionListener connectionListener);
    void removeConnectionListener();

    interface ConnectionListener {
        void onApiConnected();

        void onApiDisconnected();
    }
}
