package com.zygne.zchart.chart.menu.indicators.creators;

import com.zygne.zchart.chart.menu.indicators.PriceGapsIndicator;
import com.zygne.zchart.chart.menu.indicators.PriceImbalanceIndicator;
import com.zygne.zchart.chart.model.chart.CandleStick;
import com.zygne.zchart.chart.model.chart.PriceGap;
import com.zygne.zchart.chart.model.chart.PriceImbalance;
import com.zygne.zchart.chart.model.data.Quote;

import java.util.ArrayList;
import java.util.List;

public class PriceGapCreator {

    public void create(Callback callback, List<Quote> quoteList, List<CandleStick> candleSticks, double scalar, int chartEnd) {

        Runnable r = () -> {

            List<PriceGap> priceGaps = new ArrayList<>();


            for (Quote q : quoteList) {

                PriceGap p = new PriceGap();

                p.setY(-(int) (q.getHigh() * scalar));
                int height = (int) (q.getRange() * scalar);
                if (height < 1) {
                    height = 1;
                }
                p.setHeight(height);
                p.setWidth(0);
                p.setId(q.getTimeStamp());


                priceGaps.add(p);
            }

            for (PriceGap p : priceGaps) {
                for (CandleStick e : candleSticks) {
                    if (p.getId() == e.getId()) {
                        p.setX(e.getX());
                        p.setWidth((e.getWidth())+50);
                        break;
                    }
                }
            }

            PriceGapsIndicator indicator = new PriceGapsIndicator(priceGaps);

            callback.onPriceGapsCreated(indicator);
        };


        Thread t = new Thread(r);

        t.start();

    }

    public interface Callback {
        void onPriceGapsCreated(PriceGapsIndicator priceGapsIndicator);
    }
}
