package com.zygne.chart.chart.menu.indicators.creators;

import com.zygne.chart.chart.menu.indicators.BarIndicator;
import com.zygne.chart.chart.model.chart.Bar;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.data.BarSerie;
import com.zygne.chart.chart.model.data.Serie;

import java.util.List;


public class BarIndicatorCreator {

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
                if(barSerie.getValue() > 0){
                    b.setColorSchema(Object2d.ColorSchema.GREEN);
                } else {
                    b.setColorSchema(Object2d.ColorSchema.RED);
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
