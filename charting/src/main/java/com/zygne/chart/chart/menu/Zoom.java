package com.zygne.chart.chart.menu;

public class Zoom {

    private static final double[] stretchScale = {
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10,
            12,
            14,
            16,
            18,
            20,
            22,
            24,
            26,
            30,
            40,
            50};


    private static final double scalar = .25d;

    private final Callback callback;
    private double currentZoom = 10;
    private int currentStretch = 10;

    public Zoom(Callback callback) {
        this.callback = callback;
    }

    public void reset() {
        currentZoom = 10;
        currentStretch = 10;
        update();
    }

    public void zoomIn() {
        currentZoom *= (1 + scalar);
        update();
    }

    public void zoomOut() {
        currentZoom *= (1 - scalar);
        update();
    }


    public void stretch(double level) {
        if (level > 1) {
            return;
        }

        if (level < 0) {
            return;
        }

        currentStretch = (int) (stretchScale.length * level);

        if (currentStretch < 0) {
            currentStretch = 0;
        }

        if (currentStretch >= stretchScale.length - 1) {
            currentStretch = stretchScale.length - 1;
            return;
        }

        update();
    }

    public void stretch() {
        if (currentStretch >= stretchScale.length - 1) {
            currentStretch = stretchScale.length - 1;
            return;
        }

        currentStretch++;

        update();
    }

    public void shrink() {
        if (currentStretch <= 0) {
            currentStretch = 0;
            return;
        }

        currentStretch--;

        update();
    }

    private void update() {
        double stretchLevel = stretchScale[currentStretch];
        callback.onZoomChanged(new ZoomDetails(currentZoom, stretchLevel));
    }

    public interface Callback {
        void onZoomChanged(ZoomDetails zoomDetails);
    }

    public record ZoomDetails(double zoomLevel, double stretchLevel) {
    }

}
