package com.zygne.stockanalyzer.domain.interactor.implementation.charting;

import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.graphics.ChartLine;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;

import java.util.ArrayList;
import java.util.List;

public class LiquidityZoneChartLineInteractor implements ChartLineInteractor {

    private final Callback callback;
    private final List<LiquidityLevel> data;

    public LiquidityZoneChartLineInteractor(Callback callback, List<LiquidityLevel> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        List<LiquidityLevel> zones = new ArrayList<>(data);

        List<ChartObject> lines = new ArrayList<>();

        if (zones.size() > 1) {
            LiquidityLevel n = zones.get(0);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 3;
            line.color = ChartLine.Color.RED;
            lines.add(line);
        }

        if (zones.size() >= 2) {
            LiquidityLevel n = zones.get(1);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 2;
            line.color = ChartLine.Color.ORANGE;
            lines.add(line);
        }

        if (zones.size() >= 3) {
            LiquidityLevel n = zones.get(2);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 2;
            line.color = ChartLine.Color.ORANGE;
            lines.add(line);
        }

        if (zones.size() >= 4) {
            LiquidityLevel n = zones.get(3);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 1;
            line.color = ChartLine.Color.YELLOW;
            lines.add(line);
        }

        if (zones.size() >= 5) {
            LiquidityLevel n = zones.get(4);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 1;
            line.color = ChartLine.Color.YELLOW;
            lines.add(line);
        }

        if (zones.size() >= 6) {

            for (int i = 5; i < zones.size(); i++) {
                LiquidityLevel e = zones.get(i);

                ChartLine line = new ChartLine();
                line.level = e.price / (double) 100;
                line.size = 1;
                line.color = ChartLine.Color.BLUE;

                lines.add(line);

            }

        }

        callback.onChartLineCreated(lines);
    }
}
