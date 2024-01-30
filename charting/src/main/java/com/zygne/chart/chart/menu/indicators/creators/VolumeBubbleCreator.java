package com.zygne.chart.chart.menu.indicators.creators;

import com.zygne.chart.chart.menu.indicators.VolumeBubbleIndicator;
import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.model.chart.VolumeBubble;

import java.util.ArrayList;
import java.util.List;

public class VolumeBubbleCreator {

    public void create(Callback callback, List<CandleStick> candleSticks){

        Runnable r = () -> {

            List<VolumeBubble> volumeBubbles = new ArrayList<>();

            for (int i = 1; i < candleSticks.size(); i++) {
                CandleStick candleStick = candleSticks.get(i);

                double percentile = candleStick.getVolumeSmaPercentile();
                if(percentile > 90){
                    int width = (int) ((candleStick.getWidth() * 6));

                    VolumeBubble volumeBubble = new VolumeBubble();

                    volumeBubble.setX(candleStick.getX() - width/2);
                    volumeBubble.setY(- (candleStick.getY() - candleStick.getHeight()) + width/2);
                    volumeBubble.setWidth(width);
                    volumeBubble.setHeight(width);
                    volumeBubble.setPercentile(percentile);

                    volumeBubbles.add(volumeBubble);
                }

            }


            VolumeBubbleIndicator indicator = new VolumeBubbleIndicator(volumeBubbles, 90);
            indicator.setzOrder(0);
            callback.onVolumeBubbleIndicatorCreated(indicator);

        };


        Thread t = new Thread(r);

        t.start();

    }

    public interface Callback{
        void onVolumeBubbleIndicatorCreated(VolumeBubbleIndicator volumeBubbleIndicator);
    }
}
