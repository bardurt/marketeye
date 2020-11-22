package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.interactor.implementation.charting.ChartLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.charting.ChartZoneInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.charting.ChartZoneInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.charting.ResistanceChartLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.PineScript2Interactor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.model.LiquidityLevel;
import com.zygne.stockalyze.domain.model.LiquiditySide;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;
import com.zygne.stockalyze.presentation.presenter.base.BasePresenter;
import com.zygne.stockalyze.presentation.presenter.base.ScriptPresenter;

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
    private final List<ChartObject> chartObjects = new ArrayList<>();
    private String symbol;

    private boolean createResistance = false;
    private boolean createSides = false;
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
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public void createScript(boolean resistance, boolean zones) {
        this.createResistance = resistance;
        this.createSides = zones;
        this.chartObjects.clear();
        this.scriptTitle = "";

        if(!createResistance && !createSides){
            view.showError("Resistance or Sides must be added to script!");
            return;
        }

        Interactor interactor;

        if(createSides){
            if(liquiditySides == null){
                view.showError("Sides not calculated");
                return;
            }
            scriptTitle += "Sides";
            interactor = new ChartZoneInteractorImpl(this, liquiditySides);
        } else {
            if(this.resistance == null){
                view.showError("Resistance not calculated");
                return;
            }
            scriptTitle += "Resistance";
            interactor = new ResistanceChartLineInteractor(executor, mainThread, this, this.resistance);
        }

        view.showError("");
        view.showLoading("Creating Script...");

        interactor.execute();
    }

    @Override
    public void onChartZoneCreated(List<ChartObject> zones) {
        chartObjects.addAll(zones);

        Interactor interactor;
        if(this.createResistance) {
            interactor = new ResistanceChartLineInteractor(executor, mainThread, this, resistance);
        } else {
            interactor = new PineScript2Interactor(executor, mainThread, this, scriptTitle, symbol, chartObjects);
        }

        interactor.execute();
    }

    @Override
    public void onScriptCreated(String script, String name) {
        view.hideLoading();
        view.onScriptCreated(script);
    }

    @Override
    public void onChartLineCreated(List<ChartObject> lines) {
        chartObjects.addAll(lines);
        new PineScript2Interactor(executor, mainThread, this, scriptTitle, symbol, chartObjects).execute();
    }
}
