package com.zygne.chart.chart.model.data;

import com.zygne.chart.chart.model.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;

import java.util.ArrayList;
import java.util.List;

public class PointAndFigureBox extends Object2d {

    private int column;
    private List<PointAndFigure> items = new ArrayList<>();

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public List<PointAndFigure> getItems() {
        return items;
    }

    @Override
    public void draw(Canvas canvas) {
        for(PointAndFigure e : items){
            if(e.getState() == PointAndFigure.BULLISH_BOX){
                canvas.setColor("#00DE00");
            } else {
                canvas.setColor("#DE0000");
            }

            canvas.drawRectangle(e.getLeft(), e.getTop(), e.getWidth(), e.getHeight(), Canvas.Fill.SOLID);


            canvas.setColor("#FFFDFD");
            canvas.drawRectangle(e.getLeft(), e.getTop(), e.getWidth(), e.getHeight(), Canvas.Fill.OUTLINE);
        }
    }
}
