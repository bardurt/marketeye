package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.model.chart.BoxContainer;
import com.zygne.chart.chart.model.chart.Button;
import com.zygne.chart.chart.model.chart.Canvas;

public class PriceIndicatorMenu extends BoxContainer {

    private Listener listener;

    private boolean menuActive = true;
    private Button showMenu;
    private Button hideMenu;
    private Button volumeProfile;
    private Button pricePressure;
    private Button pricePressurePlus;
    private Button pricePressureMinus;
    private Button priceImbalance;
    private Button priceImbalancePlus;
    private Button priceImbalanceMinus;
    private Button volume;
    private Button volumeBubble;
    private Button volumeBubblePlus;
    private Button volumeBubbleMinus;


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

        volumeProfile = new Button(x, row, this.width, 30);
        volumeProfile.setText("VP");

        row += 30 + 5;

        pricePressure = new Button(x, row, this.width, 30);
        pricePressure.setText("VAP");

        row += 30;

        pricePressurePlus = new Button(x, row, this.width / 2, 25);
        pricePressurePlus.setText("+");

        pricePressureMinus = new Button(x + this.width / 2, row, this.width / 2, 25);
        pricePressureMinus.setText("-");

        row += 30;

        priceImbalance = new Button(x, row, this.width, 30);
        priceImbalance.setText("Imbalance");

        row += 30;

        priceImbalancePlus = new Button(x, row, this.width / 2, 25);
        priceImbalancePlus.setText("+");

        priceImbalanceMinus = new Button(x + this.width / 2, row, this.width / 2, 25);
        priceImbalanceMinus.setText("-");

        row += 30;

        volume = new Button(x, row, this.width, 30);
        volume.setText("Volume");

        row += 30 + 5;

        volumeBubble = new Button(x, row, this.width, 30);
        volumeBubble.setText("Activity");

        row += 30;

        volumeBubblePlus = new Button(x, row, this.width / 2, 25);
        volumeBubblePlus.setText("+");

        volumeBubbleMinus = new Button(x + this.width / 2, row, this.width / 2, 25);
        volumeBubbleMinus.setText("-");


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

        if (volumeProfile.inside(x, y)) {
            if (listener != null) {
                listener.toggleIndicator(Indicator.VOLUME_PROFILE);
            }
            return true;
        }

        if (pricePressure.inside(x, y)) {
            if (listener != null) {
                listener.toggleIndicator(Indicator.PRICE_PRESSURE);
            }
            return true;
        }

        if (priceImbalance.inside(x, y)) {
            if (listener != null) {
                listener.toggleIndicator(Indicator.PRICE_IMBALANCE);
            }
            return true;
        }

        if (priceImbalancePlus.inside(x, y)) {
            if (listener != null) {
                listener.increase(Indicator.PRICE_IMBALANCE);
            }
            return true;
        }

        if (priceImbalanceMinus.inside(x, y)) {
            if (listener != null) {
                listener.decrease(Indicator.PRICE_IMBALANCE);
            }
            return true;
        }

        if (volume.inside(x, y)) {
            if (listener != null) {
                listener.toggleIndicator(Indicator.VOLUME);
            }
            return true;
        }

        if (volumeBubble.inside(x, y)) {
            if (listener != null) {
                listener.toggleIndicator(Indicator.VOLUME_BUBBLE);
            }
            return true;
        }

        if (volumeBubblePlus.inside(x, y)) {
            if (listener != null) {
                listener.increase(Indicator.VOLUME_BUBBLE);
            }
            return true;
        }

        if (volumeBubbleMinus.inside(x, y)) {
            if (listener != null) {
                listener.decrease(Indicator.VOLUME_BUBBLE);
            }
            return true;
        }

        if (pricePressurePlus.inside(x, y)) {
            if (listener != null) {
                listener.increase(Indicator.PRICE_PRESSURE);
            }
            return true;
        }

        if (pricePressureMinus.inside(x, y)) {
            if (listener != null) {
                listener.decrease(Indicator.PRICE_PRESSURE);
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

        if (volumeProfile != null) {
            volumeProfile.draw(canvas);
        }

        if (priceImbalance != null) {
            priceImbalance.draw(canvas);
        }

        if (pricePressure != null) {
            pricePressure.draw(canvas);
        }

        if (volume != null) {
            volume.draw(canvas);
        }

        if (volumeBubble != null) {
            volumeBubble.draw(canvas);
        }

        if (volumeBubblePlus != null) {
            volumeBubblePlus.draw(canvas);
        }

        if (volumeBubbleMinus != null) {
            volumeBubbleMinus.draw(canvas);
        }

        if (pricePressurePlus != null) {
            pricePressurePlus.draw(canvas);
        }

        if (pricePressureMinus != null) {
            pricePressureMinus.draw(canvas);
        }

        if (priceImbalancePlus != null) {
            priceImbalancePlus.draw(canvas);
        }

        if(priceImbalanceMinus != null){
            priceImbalanceMinus.draw(canvas);
        }
    }

    public interface Listener {
        void toggleIndicator(Indicator indicator);

        void increase(Indicator indicator);

        void decrease(Indicator indicator);
    }

    public enum Indicator {
        VOLUME_PROFILE,
        PRICE_PRESSURE,
        PRICE_IMBALANCE,
        PRICE_GAPS,
        VOLUME,
        VOLUME_BUBBLE
    }
}