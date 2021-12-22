package com.zygne.zchart.chart.menu.indicators.creators;

import com.zygne.zchart.chart.menu.indicators.CandleSticksIndicator;
import com.zygne.zchart.chart.model.chart.CandleStick;
import com.zygne.zchart.chart.model.data.Quote;

import java.util.ArrayList;
import java.util.List;

public class CandleSticksCreator {

    public void create(Callback callback, List<Quote> quoteList, double scalar, int barWidth, int separator) {

        Runnable r = () -> {

            List<CandleStick> candleSticks = new ArrayList<>();

            int x = 0;
            int loadY = (int) (quoteList.get(quoteList.size()-1).getHigh() * scalar);
            for (Quote e : quoteList) {

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
