package com.zygne.zchart.chart.menu.indicators.creators;

import com.zygne.zchart.chart.menu.indicators.PriceImbalanceIndicator;
import com.zygne.zchart.chart.model.chart.CandleStick;
import com.zygne.zchart.chart.model.chart.PriceImbalance;
import com.zygne.zchart.chart.model.data.Quote;

import java.util.ArrayList;
import java.util.List;

public class PriceImbalanceCreator {

    public void create(Callback callback, List<Quote> quoteList, List<CandleStick> candleSticks, double scalar, int padding) {

        Runnable r = () -> {

            List<PriceImbalance> imbalances = new ArrayList<>();


            for (Quote q : quoteList) {

                PriceImbalance p = new PriceImbalance();

                p.setY(-(int) (q.getHigh() * scalar));
                int height = (int) (q.getRange() * scalar);
                if (height < 1) {
                    height = 1;
                }
                p.setHeight(height);
                p.setX(0);
                p.setWidth(1);
                p.setId(q.getTimeStamp());


                imbalances.add(p);
            }

            for (PriceImbalance p : imbalances) {
                for (CandleStick e : candleSticks) {
                    if (p.getId() == e.getId()) {
                        p.setX(e.getX());
                        p.setWidth(e.getWidth());
                        break;
                    }
                }
            }

            PriceImbalanceIndicator indicator = new PriceImbalanceIndicator(imbalances);

            callback.onPriceImbalanceCreated(indicator);
        };


        Thread t = new Thread(r);

        t.start();

    }

    public interface Callback {
        void onPriceImbalanceCreated(PriceImbalanceIndicator priceImbalanceIndicator);
    }
}
