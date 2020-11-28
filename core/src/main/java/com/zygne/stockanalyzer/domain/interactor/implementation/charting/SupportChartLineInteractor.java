package com.zygne.stockanalyzer.domain.interactor.implementation.charting;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.graphics.ChartLine;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;

import java.util.ArrayList;
import java.util.List;

public class SupportChartLineInteractor extends BaseInteractor implements ChartLineInteractor {

    private final Callback callback;
    private final List<LiquidityLevel> data;

    public SupportChartLineInteractor(Executor executor, MainThread mainThread, Callback callback, List<LiquidityLevel> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void run() {

        List<ChartObject> lines = new ArrayList<>();

        for (LiquidityLevel e : data) {

            ChartLine line = new ChartLine();
            line.level = e.price;
            line.size = 1;
            line.color = ChartObject.Color.GREEN;

            lines.add(line);

        }

        mainThread.post(() -> callback.onChartLineCreated(lines));
    }
}
