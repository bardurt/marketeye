package com.zygne.zchart.chart.menu.indicators.creators;

import com.zygne.zchart.chart.menu.indicators.VolumeProfileIndicator;
import com.zygne.zchart.chart.model.chart.VolumeProfileLine;
import com.zygne.zchart.chart.model.data.PriceBox;
import com.zygne.zchart.chart.model.data.Quote;
import com.zygne.zchart.chart.model.data.VolumeProfileGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolumeProfileCreator {

    private double scale = 1;
    private int barHeight = 2;
    private double grouping = 0.1;
    private long highestValue;
    private int y = 0;

    public void create(Callback callback, List<Quote> quoteList, double scalar, int indicatorWidth, int x) {

        Runnable r = () -> {

            this.scale = scalar;

            this.grouping = barHeight / scale;

            if(grouping < 0.01){
                grouping = 0.01;
            }

            Collections.sort(quoteList, new Quote.PriceComparator(Quote.PriceComparator.SORT_ORDER_HIGH));
            Collections.reverse(quoteList);
            double maxValue = (quoteList.get(0).getHigh() * 1.05);
            double minValue = ((quoteList.get(quoteList.size() - 1)).getHigh());


            List<VolumeProfileGroup> volumeProfileGroups = new ArrayList<>();
            double start = Math.floor(minValue);
            while (start < maxValue) {

                VolumeProfileGroup b = new VolumeProfileGroup();
                PriceBox priceBox = new PriceBox();
                priceBox.setStart(start);
                priceBox.setEnd(start + grouping);
                b.setPriceBox(priceBox);
                volumeProfileGroups.add(b);
                start += grouping;
            }


            for (int i = 0; i < quoteList.size(); i++) {
                Quote q = quoteList.get(i);

                for (int j = 0; j < volumeProfileGroups.size(); j++) {
                    VolumeProfileGroup g = volumeProfileGroups.get(j);

                    if (g.getPriceBox().inside(q.getHigh())) {
                        g.setVolume(g.getVolume() + q.getVolume());
                    }
                }
            }


            Collections.sort(volumeProfileGroups, new VolumeProfileGroup.VolumeComparator());
            Collections.reverse(volumeProfileGroups);

            if (!volumeProfileGroups.isEmpty()) {
                highestValue = volumeProfileGroups.get(0).getVolume();
            }

            Collections.sort(volumeProfileGroups, new VolumeProfileGroup.PriceComparator());
            Collections.reverse(volumeProfileGroups);


            List<VolumeProfileLine> volumeProfileLines = new ArrayList<>();

            for (int i = 0; i < volumeProfileGroups.size(); i++) {
                VolumeProfileGroup vp = volumeProfileGroups.get(i);

                VolumeProfileLine b = new VolumeProfileLine();
                b.setY((int) (-vp.getPrice() * scale));

                b.setHeight(barHeight);

                double percent = (vp.getVolume() / (double) highestValue);
                int barWidth = (int) (indicatorWidth * percent);
                int barX = x + (indicatorWidth - barWidth);
                b.setWidth(barWidth);
                b.setX(barX);

                b.setzOrder(0);

                y -= barHeight;


                if (barWidth > 0) {
                    volumeProfileLines.add(b);
                }

            }

            VolumeProfileIndicator volumeProfileIndicator = new VolumeProfileIndicator(volumeProfileLines);

            callback.onVolumeProfileCreated(volumeProfileIndicator);


        };


        Thread t = new Thread(r);

        t.start();

    }

    public interface Callback {

        void onVolumeProfileCreated(VolumeProfileIndicator volumeProfileIndicator);
    }
}
