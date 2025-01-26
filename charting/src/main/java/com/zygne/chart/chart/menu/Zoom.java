package com.zygne.chart.chart.menu;

public class Zoom {


    private static final double scalar = .25d;

    private final Callback callback;
    private double currentZoom = 10;
    private double currentStretch = 10;

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

    public void stretch() {
        currentStretch *= (1 + scalar);
        update();
    }

    public void shrink() {
        currentStretch *= (1 - scalar);
        if(currentStretch < 1){
            currentStretch = 1;
        }
        update();
    }

    private void update() {
        callback.onZoomChanged(new ZoomDetails(currentZoom, currentStretch));
    }

    public interface Callback {
        void onZoomChanged(ZoomDetails zoomDetails);
    }

    public record ZoomDetails(double zoomLevel, double stretchLevel) {
    }

}
