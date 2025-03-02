package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.data.CandleSerie;

import java.util.ArrayList;
import java.util.List;

public class CandleSticksIndicator extends Object2d {

    private final List<CandleStick> candleSticks;

    public CandleSticksIndicator(List<CandleStick> candleSticks) {
        this.candleSticks = candleSticks;
    }

    public List<CandleStick> getCandleSticks() {
        return candleSticks;
    }

    @Override
    public void draw(Canvas canvas) {
        for (CandleStick e : candleSticks) {
            e.draw(canvas);
        }
    }

    public void adjust(Creator.UpdateListener updateListener, int barWidth, double zoom) {
        final int width = (barWidth > 0) ? barWidth : 1; // Use fallback width if barWidth is 0

        int xPos = 0;
        int loadY = 0;
        for (CandleStick c : candleSticks) {
            c.adjust(zoom);
            c.setWidth(width);          // Adjust width
            c.setX(xPos);               // Set new position
            xPos += width;
            loadY = c.getY();
        }

        updateListener.onUpdated(xPos, loadY);
    }

    public static final class Creator {
        public void create(Callback callback, List<CandleSerie> quoteList, double scalar, int barWidth) {

            final int width;
            if (barWidth == 0) {
                width = 1;
            } else {
                width = barWidth;
            }


            Runnable r = () -> {

                List<CandleStick> candleSticks = new ArrayList<>();

                int x = 0;
                int loadY = (int) (quoteList.get(quoteList.size() - 1).getHigh() * scalar);
                for (CandleSerie e : quoteList) {

                    CandleStick b = new CandleStick();
                    b.ohlc(e.getOpen(),
                            e.getHigh(),
                            e.getLow(),
                            e.getClose(),
                            e.getVolume(),
                            scalar);

                    b.setX(x);
                    b.setWidth(width);

                    b.setId(e.getTimeStamp());
                    b.setTimeStamp(e.getTimeStamp());
                    b.setVolumeSma(e.getVolumeSma());
                    b.setVolumeSmaPercentile(e.getVolumeSmaPercentile());

                    b.setzOrder(0);
                    candleSticks.add(b);

                    x += width;
                }

                int lastBar = x;

                CandleSticksIndicator candleSticksIndicator = new CandleSticksIndicator(candleSticks);

                callback.onCandleSticksIndicatorCreated(candleSticksIndicator, lastBar, loadY);
            };


            Thread t = new Thread(r);

            t.start();

        }

        public interface Callback {
            void onCandleSticksIndicatorCreated(CandleSticksIndicator candleSticksIndicator, int x, int y);
        }

        public interface UpdateListener {
            void onUpdated(int x, int y);
        }
    }
}
