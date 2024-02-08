package com.zygne.chart.chart.menu.indicators.creators;

import com.zygne.chart.chart.menu.indicators.TimeIndicator;
import com.zygne.chart.chart.model.chart.*;
import com.zygne.chart.chart.model.data.TimeBox;

import java.util.ArrayList;
import java.util.List;

public class TimeCreator {

    public void create(Callback callback, List<CandleStick> candleSticks, int y, int height) {

        Runnable r = () -> {

            TimeBox timeBox = new TimeBox(candleSticks.get(0).getTimeStamp());
            TimeBar timeBar = new TimeBar();
            timeBar.setX(candleSticks.get(0).getX() - 30);
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

    public void create(Callback callback, Line line, int y, int height, int barWidth) {

        Runnable r = () -> {

            TimeBox timeBox = new TimeBox(line.getPoint(0).getTimeStamp());
            TimeBar timeBar = new TimeBar();
            timeBar.setX(line.getPoint(0).getX());
            timeBar.setY(y - height);
            timeBar.setText(timeBox.getDayString() + "\n" + timeBox.getMonthName(), TextObject.FontSize.SMALL);
            timeBar.setHeight(height);
            timeBar.setWidth(barWidth);

            List<TimeBar> timeBarList = new ArrayList<>();
            int count = 1;
            int whenToAdd = 5;

            if (barWidth < 7) {
                whenToAdd = 7;
            } else if (barWidth < 10) {
                whenToAdd = 6;
            } else if (barWidth < 15) {
                whenToAdd = 3;
            } else if (barWidth < 20) {
                whenToAdd = 2;
            }

            for (int i = 1; i < line.getPoints().size(); i++) {

                Point2d point2d = line.getPoint(i);
                timeBar.setWidth(barWidth);

                count++;
                if (count == whenToAdd) {
                    timeBarList.add(timeBar);
                    count = 0;
                }

                timeBox = new TimeBox(point2d.getTimeStamp());
                timeBar = new TimeBar();
                timeBar.setX(point2d.getX());
                timeBar.setY(y - height);
                timeBar.setText(timeBox.getDayString() + "\n" + timeBox.getMonthName(), TextObject.FontSize.SMALL);
                timeBar.setHeight(height);
            }

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
