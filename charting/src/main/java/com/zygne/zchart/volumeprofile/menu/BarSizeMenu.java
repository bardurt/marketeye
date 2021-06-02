package com.zygne.zchart.volumeprofile.menu;

import com.zygne.zchart.volumeprofile.model.chart.BoxContainer;
import com.zygne.zchart.volumeprofile.model.chart.Button;
import com.zygne.zchart.volumeprofile.model.chart.Canvas;
import com.zygne.zchart.volumeprofile.model.chart.TextObject;

public class BarSizeMenu extends BoxContainer {

    private Listener listener;
    private TextObject textObject;
    private Button largeButton;
    private Button mediumButton;
    private Button smallButton;

    public void init() {
        textObject = new TextObject();
        textObject.setText("Bar Size");
        textObject.setX(x + 5);
        textObject.setY(y);
        textObject.setWidth(width - 5);
        textObject.setHeight(20);
        textObject.setFontSize(TextObject.FontSize.SMALL);
        textObject.setAlignment(TextObject.Alignment.CENTER);

        largeButton = new Button(x, y + 30, 60, 30);
        largeButton.setText("5 pt");

        mediumButton = new Button(x, y + 80, 60, 30);
        mediumButton.setText("3 pt");

        smallButton = new Button(x, y + 130, 60, 30);
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

        if (textObject != null) {
            textObject.draw(canvas);
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
    }
}