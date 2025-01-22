package com.zygne.chart.chart.menu.indicators;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.CandleStick;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.VolumeProfileLine;
import com.zygne.chart.chart.model.data.PriceBox;
import com.zygne.chart.chart.model.data.VolumeProfileGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolumeProfileIndicator extends Object2d {

    private final List<VolumeProfileLine> volumeProfileLines;

    public VolumeProfileIndicator(List<VolumeProfileLine> volumeProfileLines) {
        this.volumeProfileLines = volumeProfileLines;
    }

    @Override
    public void draw(Canvas canvas) {
        for (VolumeProfileLine e : volumeProfileLines) {
            e.draw(canvas);
        }
    }

    public static final class Creator {

        private double scale = 1;
        private final int barHeight = 2;
        private double grouping = 0.1;
        private long highestValue;

        public void create(Callback callback, List<CandleStick> data, double scalar, int indicatorWidth, int x) {

            Runnable r = () -> {

                this.scale = scalar;

                this.grouping = barHeight / scale;

                double maxValue = Double.MIN_VALUE;
                double minValue = Double.MAX_VALUE;

                for (CandleStick c : data) {
                    if (c.visible) {
                        if (c.getPriceBox().getEnd() > maxValue) {
                            maxValue = c.getPriceBox().getEnd();
                        }
                        if (c.getPriceBox().getEnd() < minValue) {
                            minValue = c.getPriceBox().getEnd();
                        }
                    }
                }

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

                int divisor = 5;
                for (CandleStick candleStick : data) {
                    if (candleStick.visible) {
                        for (VolumeProfileGroup group : volumeProfileGroups) {

                            long vol =candleStick.getVolume();

                            if(vol == 0L){
                                vol = 1000;
                            } else {
                                vol = vol / divisor;
                            }

                            if (group.getPriceBox().inside(candleStick.getPriceBox().getPercentile(100))) {
                                group.incrementVolume(vol);
                            }

                            if (group.getPriceBox().inside(candleStick.getPriceBox().getPercentile(75))) {
                                group.incrementVolume(vol);
                            }

                            if (group.getPriceBox().inside(candleStick.getPriceBox().getPercentile(50))) {
                                group.incrementVolume(vol);
                            }

                            if (group.getPriceBox().inside(candleStick.getPriceBox().getPercentile(25))) {
                                group.incrementVolume(vol);
                            }

                            if (group.getPriceBox().inside(candleStick.getPriceBox().getPercentile(0))) {
                                group.incrementVolume(vol);
                            }
                        }
                    }
                }

                volumeProfileGroups.sort(new VolumeProfileGroup.VolumeComparator());
                Collections.reverse(volumeProfileGroups);

                if (!volumeProfileGroups.isEmpty()) {
                    highestValue = volumeProfileGroups.get(0).getVolume();
                }

                volumeProfileGroups.sort(new VolumeProfileGroup.PriceComparator());
                Collections.reverse(volumeProfileGroups);

                List<VolumeProfileLine> volumeProfileLines = new ArrayList<>();

                for (VolumeProfileGroup vp : volumeProfileGroups) {
                    VolumeProfileLine b = new VolumeProfileLine();
                    b.setColorSchema(ColorSchema.BLUE);
                    b.setY((int) (-vp.getPrice() * scale));

                    b.setHeight(barHeight);

                    double percent = (vp.getVolume() / (double) highestValue);
                    int barWidth = (int) (indicatorWidth * percent);
                    int barX = x + (indicatorWidth - barWidth);
                    b.setWidth(barWidth);
                    b.setX(barX);

                    b.setzOrder(0);

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
}
