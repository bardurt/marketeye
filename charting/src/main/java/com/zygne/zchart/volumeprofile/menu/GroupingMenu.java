package com.zygne.zchart.volumeprofile.menu;

import com.zygne.zchart.volumeprofile.model.chart.BoxContainer;
import com.zygne.zchart.volumeprofile.model.chart.Button;
import com.zygne.zchart.volumeprofile.model.chart.Canvas;
import com.zygne.zchart.volumeprofile.model.chart.TextObject;

public class GroupingMenu extends BoxContainer {

    private Listener listener;
    private TextObject textObject;
    private Button increaseButton;
    private Button decreaseButton;

    public void init() {
        textObject = new TextObject();
        textObject.setText("Grouping");
        textObject.setX(x + 5);
        textObject.setY(y);
        textObject.setWidth(width - 5);
        textObject.setHeight(20);
        textObject.setFontSize(TextObject.FontSize.SMALL);
        textObject.setAlignment(TextObject.Alignment.CENTER);

        increaseButton = new Button(x, y + 30, 60, 30);
        increaseButton.setText("++");

        decreaseButton = new Button(x, y + 80, 60, 30);
        decreaseButton.setText("--");

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        if (increaseButton != null) {
            increaseButton.draw(canvas);
        }

        if (decreaseButton != null) {
            decreaseButton.draw(canvas);
        }

        if (textObject != null) {
            textObject.draw(canvas);
        }
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

    public interface Listener {
        void onIncreaseSelected();

        void onDecreaseSelected();
    }
}
