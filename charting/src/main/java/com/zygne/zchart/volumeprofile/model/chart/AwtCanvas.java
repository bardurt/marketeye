package com.zygne.zchart.volumeprofile.model.chart;

import java.awt.*;

public class AwtCanvas implements Canvas {
    private Graphics2D graphics2D;

    public AwtCanvas(Graphics graphics) {
        this.graphics2D = (Graphics2D) graphics.create();

    }

    @Override
    public int getWidth() {
        return graphics2D.getClipBounds().width;
    }

    @Override
    public int getHeight() {
        return graphics2D.getClipBounds().height;
    }

    @Override
    public void setColor(String hexColor) {
        graphics2D.setColor(java.awt.Color.decode(hexColor));
    }

    @Override
    public void translate(int x, int y) {
        graphics2D.translate(x, y);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, Fill fill) {

        if (fill == Fill.SOLID) {
            graphics2D.fillRect(x, y, width, height);
        } else {
            graphics2D.drawRect(x, y, width, height);
        }

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, LineStyle lineStyle) {

        if (lineStyle == LineStyle.SOLID) {
            graphics2D.drawLine(x1, y1, x2, y2);
        } else {

            Stroke defaultStroke;

            defaultStroke = graphics2D.getStroke();

            // Set the stroke of the copy, not the original
            Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0, new float[]{9}, 1);
            graphics2D.setStroke(dashed);
            graphics2D.drawLine(x1, y1, x2, y2);

            graphics2D.setStroke(defaultStroke);
        }
    }

    @Override
    public void drawString(String text, int xPos, int yPos) {
        graphics2D.drawString(text, xPos, yPos);
    }

    @Override
    public void drawString(String text, int xPos, int yPos, int fontSize) {
        graphics2D.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        graphics2D.drawString(text, xPos, yPos);
    }
}
