package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class Camera extends Object2d {

    private int viewPortX;
    private int viewPortY;
    private double zoom = 1;

    public Camera(int viewPortX, int viewPortY) {
        this.viewPortX = viewPortX;
        this.viewPortY = viewPortY;
    }

    public int getViewPortX() {
        return viewPortX;
    }

    public void setViewPortX(int viewPortX) {
        this.viewPortX = viewPortX;
    }

    public int getViewPortY() {
        return viewPortY;
    }

    public void setViewPortY(int viewPortY) {
        this.viewPortY = viewPortY;
    }

    public double getZoom(){
        return zoom;
    }

    public void setZoom(double zoom){
        this.zoom = zoom;
    }

    @Override
    public int getTop() {
        return y - viewPortY;
    }

    @Override
    public int getLeft() {
        return x - viewPortX;
    }

    @Override
    public int getBottom() {
        return (super.getBottom() - viewPortY);
    }

    @Override
    public int getRight() {
        return (super.getRight()) - viewPortX;
    }

    public float getMouseX(float mouseX){
        return mouseX - viewPortX;
    }

    public float getMouseY(float mouseY){
        return mouseY - viewPortY;
    }

    @Override
    public void draw(Canvas canvas) {
    }
}
