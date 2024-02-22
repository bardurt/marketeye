package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Bar;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;

import java.util.ArrayList;
import java.util.List;

public class BarIndicator extends Object2d {

    private final List<Bar> bars = new ArrayList<>();

    public void addBar(Bar b) {
        this.bars.add(b);
    }

    public List<Bar> getBars(){
        return bars;
    }

    @Override
    public void draw(Canvas canvas) {
        for (Bar b : bars) {
            b.draw(canvas);
        }
    }

    public static final class Creator {

        public void create(Callback callback, List<Serie> data, double scalar, int barWidth) {

            Runnable r = () -> {

                BarIndicator barIndicator = new BarIndicator();

                int x = 0;
                int loadY = 0;
                for (Serie e : data) {

                    BarSerie barSerie = (BarSerie) e;

                    Bar b = new Bar();
                    b.setX(x);
                    b.setWidth(barWidth);
                    b.setzOrder(0);
                    b.setHeight((int) (-barSerie.getValue() * scalar));
                    b.setTimeStamp(barSerie.getTimeStamp());

                    if(barSerie.isIncluded()) {
                        if (barSerie.getValue() > 0) {
                            b.setColorSchema(Object2d.ColorSchema.GREEN);
                        } else {
                            b.setColorSchema(Object2d.ColorSchema.RED);
                        }
                    } else {
                        b.setColorSchema(ColorSchema.BLUE);
                    }

                    barIndicator.addBar(b);
                    x += barWidth;
                    x += 1;
                }

                int lastBar = x;

                callback.onBarIndicatorCreated(barIndicator, lastBar, loadY);
            };


            Thread t = new Thread(r);

            t.start();

        }

        public interface Callback {
            void onBarIndicatorCreated(BarIndicator barIndicator,
                                       int x, int y);
        }
    }
}