package com.zygne.zchart.volumeprofile;

import com.zygne.zchart.volumeprofile.model.chart.Camera;
import com.zygne.zchart.volumeprofile.model.chart.Canvas;
import com.zygne.zchart.volumeprofile.model.chart.Object2d;

import java.awt.*;
import java.util.List;

public class RendererImpl implements Renderer {

    private Canvas canvas;
    private Camera camera;

    public RendererImpl(Camera camera) {
        this.camera = camera;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void Render(List<Object2d> object2dList) {
        drawCanvas(object2dList);
    }

    private void drawCanvas(List<Object2d> object2dList){
        canvas.setColor("#02123B");
        canvas.drawRectangle(0, 0, camera.getWidth(), camera.getHeight(), Canvas.Fill.SOLID);

        for(Object2d object2d : object2dList){
            if (object2d.getzOrder() == -1){
                object2d.draw(canvas);
            }
        }

        // Create a copy of the Graphics instance
        canvas.translate(camera.getViewPortX(), camera.getViewPortY());

        for(Object2d object2d : object2dList){
            if (object2d.getzOrder() == 0){
                if(camera.intersects(object2d)) {
                    object2d.draw(canvas);
                }
            }
        }

        canvas.translate(-camera.getViewPortX(), -camera.getViewPortY());

        for(Object2d object2d : object2dList){
            if (object2d.getzOrder() == 1){
                object2d.draw(canvas);
            }
        }
    }
}
