package com.zygne.stockanalyzer.domain.interactor.implementation.charting;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.graphics.ChartLine;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;

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

            if(e.getPercentile() > 99){
                line.color = ChartLine.Color.RED;
            } else if(e.getPercentile() > 98){
                line.color = ChartLine.Color.ORANGE;
            } else {
                line.color = ChartLine.Color.BLUE;
            }

            lines.add(line);

        }

        mainThread.post(() -> callback.onChartLineCreated(lines));

    }
}
