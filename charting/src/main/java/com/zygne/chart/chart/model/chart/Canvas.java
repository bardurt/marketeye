package com.zygne.chart.chart.model.chart;

public interface Canvas {

    int getWidth();

    int getHeight();

    void setColor(String hexColor);

    void setColor(int r, int g, int b, int a);

    void translate(int x, int y);

    void scale(double x, double y);

    void drawRectangle(int x, int y, int width, int height, Fill fill);

    void drawCircle(int x, int y, int width, int height, Fill fill);

    void drawLine(int x1, int y1, int x2, int y2, LineStyle lineStyle);

    void drawString(String text, int xPos, int yPos);

    void drawString(String text, int xPos, int yPos, int fontSize);

    void drawString(String text, int xPos, int yPos, int fontSize, TextStyle textStyle);

    enum LineStyle {
        SOLID,
        DASHED
    }

    enum Fill {
        SOLID,
        OUTLINE
    }

    enum TextStyle{
        NORMAL,
        BOLD
    }
}
