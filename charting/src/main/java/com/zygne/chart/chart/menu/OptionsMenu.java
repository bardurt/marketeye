package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.model.chart.BoxContainer;
import com.zygne.chart.chart.model.chart.Button;
import com.zygne.chart.chart.Canvas;

public class OptionsMenu extends BoxContainer {

    private Listener listener;

    private Button scaleUpButton;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void init() {


        int row = y;

        scaleUpButton = new Button(x, y, this.width, 20);
        scaleUpButton.setText("Center");

    }

    @Override
    public boolean inside(float x, float y) {

        if (scaleUpButton.inside(x, y)) {
            if (listener != null) {
                listener.onOptionsSelected(OptionItem.CENTER_CHART);
            }
            return true;
        }


        return false;
    }

    @Override
    public void draw(Canvas canvas) {


        if (scaleUpButton != null) {
            scaleUpButton.draw(canvas);
        }

    }

    public interface Listener {
        void onOptionsSelected(OptionItem options);
    }

    public enum OptionItem {
        CENTER_CHART
    }
}