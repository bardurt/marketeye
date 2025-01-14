package com.zygne.chart.chart.menu;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class ChartControls extends MouseInputAdapter {

    private static final int THRESHOLD = 50;
    private int startY;
    private int startX;
    private boolean scaling = false;

    private final Callback callback;

    public ChartControls(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            scaling = true;
        }

        startY = e.getY();
        startX = e.getX();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            return;
        }
        scaling = false;

        int dy = startY - e.getY();
        int dx = startX - e.getX();

        int absDy = Math.abs(dy);
        int absDx = Math.abs(dx);

        if (absDy > absDx) {
            if (dy > THRESHOLD) {
                callback.onDragUp(absDy);

            }
            if (dy < -THRESHOLD) {
                callback.onDragDown(absDy);
            }
        } else {
            if (dx > THRESHOLD) {
                callback.onDragLeft(absDx);
            }
            if (dx < -THRESHOLD) {
                callback.onDragRight(absDx);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (scaling) {
            return;
        }
        int dy = startY - e.getY();
        int dx = startX - e.getX();

        callback.onDrag(dx, dy);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public interface Callback {
        void onDragLeft(int dist);

        void onDragRight(int dist);

        void onDragUp(int dist);

        void onDragDown(int dist);

        void onDrag(int dx, int dy);
    }
}
