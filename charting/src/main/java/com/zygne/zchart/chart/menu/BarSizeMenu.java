package com.zygne.zchart.chart.menu;

import com.zygne.zchart.chart.model.chart.BoxContainer;
import com.zygne.zchart.chart.model.chart.Button;
import com.zygne.zchart.chart.model.chart.Canvas;

public class BarSizeMenu extends BoxContainer {

    private Listener listener;
    private Button largeButton;
    private Button mediumButton;
    private Button smallButton;

    public void init() {
        largeButton = new Button(x, y + 30, 50, 20);
        largeButton.setText("5 pt");

        mediumButton = new Button(x + 55, y + 30, 50, 20);
        mediumButton.setText("3 pt");

        smallButton = new Button(x + 55 + 55, y + 30, 50, 20);
        smallButton.setText("1 pt");

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        if (largeButton != null) {
            largeButton.draw(canvas);
        }

        if (mediumButton != null) {
            mediumButton.draw(canvas);
        }

        if (smallButton != null) {
            smallButton.draw(canvas);
        }
    }

    @Override
    public boolean inside(float x, float y) {
        if (largeButton.inside(x, y)) {
            if (listener != null) {
                listener.onBarSizeChanged(5);
            }
            return true;
        }

        if (mediumButton.inside(x, y)) {
            if (listener != null) {
                listener.onBarSizeChanged(3);
            }
            return true;
        }

        if (smallButton.inside(x, y)) {
            if (listener != null) {
                listener.onBarSizeChanged(1);
            }
            return true;
        }

        return false;
    }

    public interface Listener {
        void onBarSizeChanged(int barSize);
        void onIncreaseSelected();
        void onDecreaseSelected();
    }
}