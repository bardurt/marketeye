package com.zygne.chart.chart.menu.indicators.creators;

import com.zygne.chart.chart.menu.indicators.PricePressureIndicator;
import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.model.chart.PricePressure;
import com.zygne.chart.chart.model.data.Quote;

import java.util.ArrayList;
import java.util.List;

public class PricePressureCreator {

    public void create(Callback callback, List<Quote> quoteList, List<CandleStick> candleSticks, double scalar, int chartEnd, int percentile) {

        Runnable r = () -> {

            List<PricePressure> pricePressures = new ArrayList<>();

            for (Quote q : quoteList) {

                if (q.getPercentile() > percentile) {

                    PricePressure p = new PricePressure();

                    p.setY(-(int) (q.getHigh() * scalar));
                    p.setHeight(1);
                    p.setX(0);
                    p.setWidth(1000);
                    p.setId(q.getTimeStamp());
                    p.setPrice(q.getHigh() * scalar);
                    p.setPercentile(q.getPercentile());

                    pricePressures.add(p);
                }
            }

            for (PricePressure p : pricePressures) {
                for (CandleStick e : candleSticks) {
                    if (e.getPriceBox().inside(p.getPrice())) {
                        p.setX(e.getX());
                        p.setWidth((50 + chartEnd) - e.getX());
                        break;
                    }
                }
            }

            PricePressureIndicator indicator = new PricePressureIndicator(pricePressures, percentile);


            callback.onPricePressureCreated(indicator);
        };


        Thread t = new Thread(r);

        t.start();

    }


    public interface Callback {
        void onPricePressureCreated(PricePressureIndicator pricePressureIndicator);
    }
}
