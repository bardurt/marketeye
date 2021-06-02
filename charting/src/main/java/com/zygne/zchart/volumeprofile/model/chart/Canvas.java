package com.zygne.zchart.volumeprofile.model.chart;

public interface Canvas {

    int getWidth();

    int getHeight();

    void setColor(String hexColor);

    void translate(int x, int y);

    void drawRectangle(int x, int y, int width, int height, Fill fill);

    void drawLine(int x1, int y1, int x2, int y2, LineStyle lineStyle);

    void drawString(String text, int xPos, int yPos);

    void drawString(String text, int xPos, int yPos, int fontSize);

    enum LineStyle {
        SOLID,
        DASHED
    }

    enum Fill {
        SOLID,
        OUTLINE
    }
}
