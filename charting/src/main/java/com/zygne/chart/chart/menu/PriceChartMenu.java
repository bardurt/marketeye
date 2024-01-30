package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.model.chart.BoxContainer;
import com.zygne.chart.chart.model.chart.Button;
import com.zygne.chart.chart.model.chart.Canvas;

public class PriceChartMenu extends BoxContainer {

    private Listener listener;

    private boolean menuActive = true;

    private Button showMenu;
    private Button hideMenu;

    private Button separatorLabel;

    private Button barSeparatorPlusButton;
    private Button barSeparatorMinusButton;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void init() {


        int row = y;

        showMenu = new Button(x, row, this.width / 2, 20);
        showMenu.setText("+");

        hideMenu = new Button(x + this.width / 2, row, this.width / 2, 20);
        hideMenu.setText("-");

        row += 35;


        separatorLabel = new Button(x, row, this.width, 30);
        separatorLabel.setText("Separator");

        row += 30;

        barSeparatorPlusButton = new Button(x, row, this.width / 2, 20);
        barSeparatorPlusButton.setText("+");

        barSeparatorMinusButton = new Button(x + this.width / 2, row, this.width / 2, 20);
        barSeparatorMinusButton.setText("-");
    }

    @Override
    public boolean inside(float x, float y) {

        if (hideMenu.inside(x, y)) {
            menuActive = false;
            return true;
        }

        if (showMenu.inside(x, y)) {
            menuActive = true;
            return true;
        }

        if (!menuActive) {
            return false;
        }

        if (barSeparatorPlusButton.inside(x, y)) {
            if (listener != null) {
                listener.scaleUp(Entity.SEPARATOR);
            }

            return true;
        }

        if (barSeparatorMinusButton.inside(x, y)) {
            if (listener != null) {
                listener.scaleDown(Entity.SEPARATOR);
            }

            return true;
        }


        return false;
    }

    @Override
    public void draw(Canvas canvas) {

        if (showMenu != null) {
            showMenu.draw(canvas);
        }

        if (hideMenu != null) {
            hideMenu.draw(canvas);
        }

        if (!menuActive) {
            return;
        }

        if (separatorLabel != null) {
            separatorLabel.draw(canvas);
        }

        if (barSeparatorPlusButton != null) {
            barSeparatorPlusButton.draw(canvas);
        }

        if (barSeparatorMinusButton != null) {
            barSeparatorMinusButton.draw(canvas);
        }
    }

    public interface Listener {
        void scaleUp(Entity entity);

        void scaleDown(Entity entity);
    }

    public enum Entity{
        WIDTH,
        HEIGHT,
        SEPARATOR
    }
}