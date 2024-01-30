package com.zygne.chart;

import javax.swing.*;

public class ChartLauncher {

    public static void main(String args[]) {
        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new ChartView2());

        frame.pack();
        frame.setVisible(true);
    }
}
