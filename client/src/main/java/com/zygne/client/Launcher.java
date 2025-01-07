package com.zygne.client;


import com.zygne.client.swing.SwingGui;

public class Launcher {

    public static void main(String[] args) {
        SwingGui.launch(ApiProps.readApi());
    }

}
