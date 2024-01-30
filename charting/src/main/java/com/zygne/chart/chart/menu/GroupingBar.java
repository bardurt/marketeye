package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.model.chart.BoxContainer;
import com.zygne.chart.chart.model.chart.Button;
import com.zygne.chart.chart.model.chart.Canvas;

public class GroupingBar extends BoxContainer {

    private Listener listener;

    private Button increaseButton;
    private Button decreaseButton;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void init() {
        increaseButton = new Button(x, y, 50, 20);
        increaseButton.setText("$ +");

        decreaseButton = new Button(x+55, y, 50, 20);
        decreaseButton.setText("$ -");
    }

    @Override
    public boolean inside(float x, float y) {
        if (increaseButton.inside(x, y)) {
            //grouping = grouping * 1.05;
            //setGrouping(grouping);
            if (listener != null) {
                listener.onIncreaseSelected();
            }
            return true;
        }

        if (decreaseButton.inside(x, y)) {
            //grouping = grouping * 0.95;
            //setGrouping(grouping);
            if (listener != null) {
                listener.onDecreaseSelected();
            }

            return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (increaseButton != null) {
            increaseButton.draw(canvas);
        }

        if (decreaseButton != null) {
            decreaseButton.draw(canvas);
        }
    }

    public interface Listener {
        void onIncreaseSelected();
        void onDecreaseSelected();
    }
}
