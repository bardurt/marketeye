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

        for (LiquidityLevel e : zones) {
            ChartLine line = new ChartLine();
            line.level = e.price / (double) 100;
            line.size = 1;
            line.color = ChartLine.Color.BLUE;

            lines.add(line);
        }

        callback.onChartLineCreated(lines);
    }
}
