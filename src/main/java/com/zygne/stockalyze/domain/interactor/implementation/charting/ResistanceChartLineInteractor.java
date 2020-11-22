package com.zygne.stockalyze.domain.interactor.implementation.charting;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.model.LiquidityLevel;
import com.zygne.stockalyze.domain.model.graphics.ChartLine;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;

import java.util.ArrayList;
import java.util.List;

public class ResistanceChartLineInteractor extends BaseInteractor implements ChartLineInteractor {

    private final Callback callback;
    private final List<LiquidityLevel> data;

    public ResistanceChartLineInteractor(Executor executor, MainThread mainThread, Callback callback, List<LiquidityLevel> data) {
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
            line.color = ChartLine.Color.BLUE;

            lines.add(line);

        }

        mainThread.post(() -> callback.onChartLineCreated(lines));

    }
}
