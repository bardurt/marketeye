package com.zygne.chart.chart.model.chart;

public class Camera extends Object2d {

    private int viewPortX = 0;
    private int viewPortY = 0;
    private int paddingWidth = 0;
    private int paddingHeight = 0;
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
        canvas.setColor("#00A33B");
        canvas.drawRectangle(viewPortX, viewPortY, width, height, Canvas.Fill.OUTLINE);
    }
}
