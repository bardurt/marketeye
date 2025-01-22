package com.zygne.chart.chart.model.chart;

public class BoxCollider {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public BoxCollider() {
    }

    public BoxCollider(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTop() {
        return y;
    }

    public int getLeft() {
        return x;
    }

    public int getBottom() {
        return y + height;
    }

    public int getRight() {
        return x + width;
    }

    public boolean inside(float x, float y) {
        return x >= this.getLeft() && x <= this.getRight() &&
                y >= this.getTop() && y <= this.getBottom();
    }


    public boolean intersects(BoxCollider boxCollider) {
        return boxCollider.getLeft() <= this.getRight() &&
                boxCollider.getRight() >= this.getLeft() &&
                boxCollider.getTop() <= this.getBottom() &&
                boxCollider.getBottom() >= this.getTop();
    }

    public boolean inHorizontalSpace(BoxCollider boxCollider) {
        return boxCollider.getLeft() <= this.getRight() &&
                boxCollider.getRight() >= this.getLeft();
    }


    @Override
    public String toString() {
        return "Top " + getTop() +
                ", Left " + getLeft() +
                ", Right " + getRight() +
                ", Bottom " + getBottom();
    }
}
