package com.zygne.chart.chart.menu.indicators.creators;

import com.zygne.chart.chart.menu.indicators.VolumeIndicator;
import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.model.chart.VolumeBar;

import java.util.ArrayList;
import java.util.List;

public class VolumeIndicatorCreator {

    public void create(Callback callback, List<CandleStick> candleSticks, int height, int y) {

        Runnable r = () -> {

            List<VolumeBar> volumeBars = new ArrayList<>();

            long highestVolume = 0;
            for (CandleStick e : candleSticks) {
                if (e.getVolumeSma() > highestVolume) {
                    highestVolume = e.getVolumeSma();
                }
            }

            for (CandleStick e : candleSticks) {
                double percent = e.getVolumeSma() / (double) highestVolume;
                int barHeight = (int) (height * percent);
                if (barHeight < 2) {
                    barHeight = 2;
                }

                VolumeBar volumeBar = new VolumeBar();
                volumeBar.setX(e.getX());
                volumeBar.setY(y);
                volumeBar.setWidth(e.getWidth());
                volumeBar.setHeight(barHeight);

                volumeBars.add(volumeBar);
            }

            VolumeIndicator indicator = new VolumeIndicator(volumeBars);
            indicator.setzOrder(3);

            callback.onVolumeIndicatorCreated(indicator);
        };

        Thread t = new Thread(r);

        t.start();
    }

    public interface Callback {
        void onVolumeIndicatorCreated(VolumeIndicator volumeIndicator);
    }
}
