package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Bar;
import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.model.chart.Line;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.Point2d;
import com.zygne.chart.chart.model.chart.TextObject;
import com.zygne.chart.chart.model.chart.TimeBar;
import com.zygne.chart.chart.model.data.TimeBox;

import java.util.ArrayList;
import java.util.List;

public class TimeIndicator extends Object2d {

    private final List<TimeBar> timeBarList;

    public TimeIndicator(List<TimeBar> timeBarList) {
        this.timeBarList = timeBarList;
    }

    @Override
    public void draw(Canvas canvas) {
        for (TimeBar e : timeBarList) {
            e.draw(canvas);
        }
    }

    public static final class Creator {
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
                int whenToAdd;

                if (barWidth < 7) {
                    whenToAdd = 7;
                } else if (barWidth < 10) {
                    whenToAdd = 6;
                } else if (barWidth < 15) {
                    whenToAdd = 3;
                } else if (barWidth < 20) {
                    whenToAdd = 2;
                } else {
                    whenToAdd = 1;
                }

                for (int i = 1; i < line.getPoints().size(); i++) {

                    Point2d point2d = line.getPoint(i);
                    timeBar.setWidth(barWidth);

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

                    count++;
                }

                TimeIndicator timeIndicator = new TimeIndicator(timeBarList);
                timeIndicator.setzOrder(3);
                callback.onTimeIndicatorCreated(timeIndicator);

            };


            Thread t = new Thread(r);

            t.start();
        }

        public void create(Callback callback, List<Bar> barSeries, int y, int height, int barWidth) {
            Runnable r = () -> {
                TimeBox timeBox = new TimeBox(barSeries.get(0).getTimeStamp());
                TimeBar timeBar = new TimeBar();
                timeBar.setX(barSeries.get(0).getX());
                timeBar.setY(y - height);
                if (barSeries.get(0).getShowMonthOnly()) {
                    timeBar.setText(timeBox.getMonthName(), TextObject.FontSize.SMALL);
                } else {
                    timeBar.setText(timeBox.getDayString() + "/" + timeBox.getMonthString() + "\n" + timeBox.getYear(), TextObject.FontSize.SMALL);
                }

                timeBar.setHeight(height);
                timeBar.setWidth(barWidth);

                List<TimeBar> timeBarList = new ArrayList<>();
                timeBarList.add(timeBar);
                int count = 1;
                int whenToAdd = 5;

                System.out.println("Barwidth = " + barWidth);
                if (barWidth < 7) {
                    whenToAdd = 7;
                } else if (barWidth < 10) {
                    whenToAdd = 6;
                } else if (barWidth < 20) {
                    whenToAdd = 3;
                } else if (barWidth < 40) {
                    whenToAdd = 2;
                } else {
                    whenToAdd = 1;
                }

                for (int i = 1; i < barSeries.size(); i++) {

                    Bar bar = barSeries.get(i);
                    timeBar.setWidth(barWidth);

                    timeBox = new TimeBox(bar.getTimeStamp());
                    timeBar = new TimeBar();
                    timeBar.setX(bar.getX());
                    timeBar.setY(y - height);
                    if (barSeries.get(0).getShowMonthOnly()) {
                        timeBar.setText(timeBox.getMonthName(), TextObject.FontSize.SMALL);
                    } else {
                        timeBar.setText(timeBox.getDayString() + "/" + timeBox.getMonthString() + "\n" + timeBox.getYear(), TextObject.FontSize.SMALL);
                    }

                    timeBar.setHeight(height);

                    if (count >= whenToAdd) {
                        timeBarList.add(timeBar);
                        count = 0;
                    }

                    count++;
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
}
