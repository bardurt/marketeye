package com.zygne.zchart.chart.menu.indicators.creators;

import com.zygne.zchart.chart.menu.indicators.TimeIndicator;
import com.zygne.zchart.chart.model.chart.CandleStick;
import com.zygne.zchart.chart.model.chart.TimeBar;
import com.zygne.zchart.chart.model.data.TimeBox;

import java.util.ArrayList;
import java.util.List;

public class TimeCreator {

    public void create(Callback callback, List<CandleStick> candleSticks, int y, int height) {

        Runnable r = () -> {

            TimeBox timeBox = new TimeBox(candleSticks.get(0).getTimeStamp());
            TimeBar timeBar = new TimeBar();
            timeBar.setX(candleSticks.get(0).getX()-30);
            timeBar.setY(y - height);
            timeBar.setText(timeBox.getMonth() + "/" + timeBox.getYear());
            timeBar.setHeight(height);
            timeBar.setWidth(100);

            List<TimeBar> timeBarList = new ArrayList<>();

            for (int i = 1; i < candleSticks.size(); i++) {

                CandleStick candleStick = candleSticks.get(i);
                if (!timeBox.isSameMonth(candleStick.getTimeStamp())) {

                    timeBar.setWidth(candleStick.getX() - timeBar.getX());
                    timeBarList.add(timeBar);

                    timeBox = new TimeBox(candleStick.getTimeStamp());
                    timeBar = new TimeBar();
                    timeBar.setX(candleStick.getX());
                    timeBar.setY(y - height);
                    timeBar.setText(timeBox.getMonth() + "/" + timeBox.getYear());
                    timeBar.setHeight(height);
                }

            }

            timeBarList.add(timeBar);


            TimeIndicator timeIndicator = new TimeIndicator(timeBarList);
            timeIndicator.setzOrder(3);
            callback.onTimeIndicatorCreated(timeIndicator);

        };


        Thread t = new Thread(r);

        t.start();

    }

    public interface Callback {
        void onTimeIndicatorCreated(TimeIndicator timeIndicator);
    }
}
