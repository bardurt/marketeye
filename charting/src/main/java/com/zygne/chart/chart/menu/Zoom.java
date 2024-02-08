package com.zygne.chart.chart.menu;

public class Zoom {

    private static final double[] zoomScales = {
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
            5000};

    private static final double[] stretchScale = {
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
            26};


    private final Callback callback;
    private int currentZoom = 10;
    private int currentStretch = 10;

    public Zoom(Callback callback) {
        this.callback = callback;
    }

    public void zoomIn() {

        if (currentZoom >= zoomScales.length - 1) {
            currentZoom = zoomScales.length - 1;
            return;
        }
        currentZoom++;

        getZoomLevel();
    }

    public void zoomOut() {

        if (currentZoom < 0) {
            currentZoom = 0;
            return;
        }
        currentZoom--;

        getZoomLevel();
    }

    private void getZoomLevel() {
        double level = zoomScales[currentZoom];
        callback.onZoomLevelSet(level);
    }

    public void stretch(){
        if(currentStretch >= stretchScale.length-1){
            currentStretch = stretchScale.length-1;
            return;
        }

        currentStretch++;

        getStretchLevel();
    }

    public void shrink(){
        if(currentStretch <= 0){
            currentStretch = 0;
            return;
        }

        currentStretch--;

        getStretchLevel();
    }

    private void getStretchLevel(){
        double level = stretchScale[currentStretch];
        System.out.println("Stretch " + level);
        callback.onStretchSet(level);
    }

    public interface Callback {
        void onZoomLevelSet(double scalar);

        void onStretchSet(double scalar);
    }
}
