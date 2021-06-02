package com.zygne.stockanalyzer.presentation.presenter.implementation;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.interactor.implementation.charting.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.scripting.PineScriptInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;
import com.zygne.stockanalyzer.presentation.presenter.base.BasePresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.ScriptPresenter;

import java.util.ArrayList;
import java.util.List;

public class ScriptPresenterImpl extends BasePresenter implements ScriptPresenter,
        ChartZoneInteractor.Callback,
        ChartLineInteractor.Callback,
        ScriptInteractor.Callback {

    private final View view;

    private List<LiquidityLevel> resistance;
    private List<LiquidityLevel> support;
    private List<LiquiditySide> liquiditySides;
    private List<PriceGap> gapList;
    private final List<ChartObject> chartObjects = new ArrayList<>();
    private String symbol;

    private String scriptTitle = "";

    public ScriptPresenterImpl(Executor executor, MainThread mainThread, View view) {
        super(executor, mainThread);
        this.view = view;
    }

    @Override
    public void setResistance(List<LiquidityLevel> liquidityLevels) {
        this.resistance = liquidityLevels;
    }

    @Override
    public void setZones(List<LiquiditySide> zones) {
        this.liquiditySides = zones;
    }

    @Override
    public void setGaps(List<PriceGap> gaps) {
        this.gapList = gaps;
    }

    @Override
    public void setSymbol(String symbol) {
        this.symbol = symbol.toUpperCase();
    }

    @Override
    public void createScript(boolean resistance, boolean zones, boolean gaps) {
        this.chartObjects.clear();
        this.scriptTitle = "";

        Interactor interactor = null;

        if (zones) {
            if (liquiditySides == null) {
                view.showError("Sides not calculated");
                return;
            }
            scriptTitle += "Sides";
            interactor = new ChartZoneInteractorImpl(executor, mainThread, this, liquiditySides);
        } else if (resistance) {
            if (this.resistance == null) {
                view.showError("Resistance not calculated");
                return;
            }
            scriptTitle += "Supply";
            interactor = new ResistanceChartLineInteractor(executor, mainThread, this, this.resistance);
        } else if (gaps) {
            if (this.gapList == null) {
                view.showError("Resistance not calculated");
                return;
            }
            scriptTitle += "Gaps";
            interactor = new PriceGapZoneInteractor(executor, mainThread, this, this.gapList);

        }

        if (interactor != null) {
            view.showError("");
            view.showLoading("Creating Script...");
            interactor.execute();
        }
    }

    @Override
    public void onChartZoneCreated(List<ChartObject> zones) {
        chartObjects.addAll(zones);
        new PineScriptInteractor(executor, mainThread, this, scriptTitle, symbol, chartObjects).execute();
    }

    @Override
    public void onScriptCreated(String script, String name) {
        view.hideLoading();
        view.onScriptCreated(script);
    }

    @Override
    public void onChartLineCreated(List<ChartObject> lines) {
        chartObjects.addAll(lines);
        new PineScriptInteractor(executor, mainThread, this, scriptTitle, symbol, chartObjects).execute();
    }
}
