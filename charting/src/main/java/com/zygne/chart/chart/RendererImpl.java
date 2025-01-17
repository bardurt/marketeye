package com.zygne.chart.chart;

import com.zygne.chart.chart.model.chart.Camera;
import com.zygne.chart.chart.model.chart.Colors;
import com.zygne.chart.chart.model.chart.Object2d;

import java.util.List;

public class RendererImpl implements Renderer {

    private Canvas canvas;
    private final Camera camera;

    public RendererImpl(Camera camera) {
        this.camera = camera;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void render(List<Object2d> object2dList) {
        drawCanvas(object2dList);
    }

    private void drawCanvas(List<Object2d> object2dList) {
        canvas.drawRectangle(0, 0, camera.getWidth(), camera.getHeight(), Canvas.Fill.SOLID);

        for (int i = 0; i < object2dList.size(); i++) {
            Object2d object2d = object2dList.get(i);

            if (object2d.getzOrder() == -1) {
                object2d.draw(canvas);
            }

            if (object2d.getzOrder() == 1) {
                canvas.translate(0, camera.getViewPortY());
                object2d.draw(canvas);
                canvas.translate(0, -camera.getViewPortY());
            }

            if (object2d.getzOrder() == 0) {
                canvas.translate(camera.getViewPortX(), camera.getViewPortY());
                object2d.draw(canvas);
                canvas.translate(-camera.getViewPortX(), -camera.getViewPortY());
            }

            if (object2d.getzOrder() == 2) {
                object2d.draw(canvas);
            }

            if (object2d.getzOrder() == 3) {
                canvas.translate(camera.getViewPortX(), 0);
                object2d.draw(canvas);
                canvas.translate(-camera.getViewPortX(), 0);
            }

            if (object2d.getzOrder() >= 4) {
                object2d.draw(canvas);
            }
        }

        canvas.setColor(Colors.BLACK);
        canvas.drawRectangle(0, 0, camera.getWidth(), camera.getHeight(), Canvas.Fill.OUTLINE);
        canvas.drawRectangle(0, 0, 2, camera.getHeight(), Canvas.Fill.SOLID);
        canvas.drawRectangle(camera.getWidth() - 2, 0, camera.getWidth(), camera.getHeight(), Canvas.Fill.SOLID);
    }

    @Override
    public boolean sizeChanged(int width, int height) {
        if (camera == null) {
            return false;
        }

        if (camera.getWidth() != width) {
            return true;
        }

        return camera.getHeight() != height;
    }

}
