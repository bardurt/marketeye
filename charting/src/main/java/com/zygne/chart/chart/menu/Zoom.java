package com.zygne.chart.chart.menu;

public class Zoom {

    private static final double[] zoomScales = {
            0.000000001d,
            0.00000001d,
            0.0000001d,
            0.000001d,
            0.00001d,
            0.0001d,
            0.001d,
            0.005d,
            0.01d,
            0.025d,
            0.05,
            0.1,
            0.25,
            0.5,
            1,
            2,
            5,
            10,
            20,
            50,
            100,
            250,
            500,
            1000,
            5000,
            10000,
            100000,
            1000000,
            10000000,
            100000000,};

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


    private final Callback callback;
    private int currentZoom = 10;
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

        if (currentZoom >= zoomScales.length - 1) {
            currentZoom = zoomScales.length - 1;
            return;
        }
        currentZoom++;

        update();
    }

    public void zoomOut() {

        if (currentZoom <= 0) {
            currentZoom = 0;
            return;
        }
        currentZoom--;

        update();
    }

    public void zoom(double level) {
        if (level > 1) {
            return;
        }

        if (level < 0) {
            return;
        }

        currentZoom = (int) (zoomScales.length * level);

        if(currentZoom < 0){
            currentZoom = 0;
        }

        if (currentZoom >= zoomScales.length - 1) {
            currentZoom = zoomScales.length - 1;
            return;
        }

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

        if(currentStretch < 0){
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
        double zoomLevel = zoomScales[currentZoom];

        callback.onZoomChanged(new ZoomDetails(zoomLevel, stretchLevel));
    }

    public interface Callback {
        void onZoomChanged(ZoomDetails zoomDetails);
    }

    public record ZoomDetails(double zoomLevel, double stretchLevel) {
    }

}
