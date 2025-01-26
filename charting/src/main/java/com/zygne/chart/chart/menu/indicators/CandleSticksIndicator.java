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

    public void adjust(int barWidth, double zoom) {
        final int width;

        if (barWidth == 0) {
            width = 1;
        } else {
            width = barWidth;
        }

        int xPos = 0;
        for (int i = 0; i < candleSticks.size(); i++) {
            CandleStick c = candleSticks.get(i);
            c.setWidth(width);
            c.setX(xPos);
            xPos += width;

            c.adjust(zoom);
        }
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
    }
}
