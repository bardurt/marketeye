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

    public static final class Creator {
        public void create(Callback callback, List<CandleSerie> quoteList, double scalar, int barWidth, int separator) {

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
                    b.setWidth(barWidth);

                    b.setId(e.getTimeStamp());
                    b.setTimeStamp(e.getTimeStamp());
                    b.setVolumeSma(e.getVolumeSma());
                    b.setVolumeSmaPercentile(e.getVolumeSmaPercentile());

                    b.setzOrder(0);
                    candleSticks.add(b);
                    candleSticks.add(b);

                    x += barWidth;
                    x += 1;
                }

                int lastBar = x;

                CandleSticksIndicator candleSticksIndicator = new CandleSticksIndicator(candleSticks);

                callback.onCandleSticksIndicatorCreated(candleSticksIndicator, lastBar, loadY);
            };


            Thread t = new Thread(r);

            t.start();

        }

        public interface Callback {
            void onCandleSticksIndicatorCreated(CandleSticksIndicator candleSticksIndicator,
                                                int x, int y);
        }
    }
}
