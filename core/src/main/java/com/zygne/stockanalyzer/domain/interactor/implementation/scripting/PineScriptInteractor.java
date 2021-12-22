package com.zygne.stockanalyzer.domain.interactor.implementation.scripting;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.model.graphics.ChartLine;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;
import com.zygne.stockanalyzer.domain.model.graphics.ChartZone;

import java.text.SimpleDateFormat;
import java.util.List;

public class PineScriptInteractor extends BaseInteractor implements ScriptInteractor {

    private static final String COLOR_GREEN = "color.green";
    private static final String COLOR_RED = "color.red";
    private static final String COLOR_ORANGE = "color.orange";
    private static final String COLOR_YELLOW = "color.yellow";
    private static final String COLOR_BLUE = "color.blue";
    private static final String COLOR_NAVY = "color.navy";
    private static final String COLOR_PINK = "color.fuchsia";

    private static final String EXTENSION = ".pine";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
    private final Callback callback;
    private final String name;
    private final String ticker;
    private final List<ChartObject> data;

    public PineScriptInteractor(Executor executor, MainThread mainThread, Callback callback, String name, String ticker, List<ChartObject> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.name = name;
        this.ticker = ticker;
        this.data = data;
    }

    @Override
    public void run() {
        String scriptName = name + "_" + ticker + EXTENSION;

        String time = simpleDateFormat.format(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("//@version=4");
        stringBuilder.append("\n");
        stringBuilder.append("// Script for TradingView PineScript Editor");
        stringBuilder.append("\n");
        stringBuilder.append("// Ticker : ").append(ticker);
        stringBuilder.append("\n");
        stringBuilder.append("// Author : Barthur").append(" - ").append(time);
        stringBuilder.append("\n");
        stringBuilder.append("study(\"").append(name).append(" -  ").append(ticker).append("\",").append(" overlay=true)");

        int id = 0;
        for (ChartObject o : data) {
            stringBuilder.append("\n");

            id++;

            stringBuilder.append("// --- " + id  + " ---");
            stringBuilder.append("\n");
            if (o instanceof ChartLine) {
                stringBuilder.append(plotLine((ChartLine) o));
            }

            if (o instanceof ChartZone) {
                stringBuilder.append(plotZone((ChartZone) o, id));
            }

        }

        stringBuilder.append("\n");

        stringBuilder.append("plot(na)");

        mainThread.post(() -> callback.onScriptCreated(stringBuilder.toString(), scriptName));
    }

    private String plotLine(ChartLine chartLine) {

        StringBuilder stringBuilder = new StringBuilder();

        int lineWidth = chartLine.size;

        stringBuilder.append("hline(");
        stringBuilder.append(chartLine.level);
        stringBuilder.append(",");
        stringBuilder.append("color=color.new("+mapColor(chartLine.color)+", 50)");
        stringBuilder.append(",");
        stringBuilder.append("linewidth=");
        stringBuilder.append(lineWidth);
        stringBuilder.append(", linestyle=hline.style_solid");
        stringBuilder.append(")");


        return stringBuilder.toString();
    }

    private String plotZone(ChartZone chartZone, int id) {

        StringBuilder stringBuilder = new StringBuilder();

        String name1 = "h" + id + "_1";
        String name2 = "h" + id + "_2";

        stringBuilder.append(name1).append("=hline(").append(chartZone.top).append(", linestyle=hline.style_dotted,");

        if (chartZone.color == ChartObject.Color.GREEN) {
            stringBuilder.append("color=color.new("+ COLOR_GREEN+", 50)");
        } else {
            stringBuilder.append("color=color.new("+ COLOR_RED+", 50)");
        }
        stringBuilder.append(")");
        stringBuilder.append("\n");

        stringBuilder.append(name2).append("=hline(").append(chartZone.bottom).append(", linestyle=hline.style_dotted,");

        if (chartZone.color == ChartObject.Color.GREEN) {
            stringBuilder.append("color=color.new("+ COLOR_GREEN+", 50)");
        } else {
            stringBuilder.append("color=color.new("+ COLOR_RED+", 59)");
        }
        stringBuilder.append(")");
        stringBuilder.append("\n");

        stringBuilder.append("fill(").append(name1).append(", ").append(name2).append(", ");

        stringBuilder.append(mapColor(chartZone.color)).append(",");

        stringBuilder.append("transp=");
        stringBuilder.append(chartZone.transparency);
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private String mapColor(int colorCode) {

        switch (colorCode) {
            case ChartObject.Color.GREEN:
                return COLOR_GREEN;
            case ChartObject.Color.RED:
                return COLOR_RED;
            case ChartObject.Color.ORANGE:
                return COLOR_ORANGE;
            case ChartObject.Color.YELLOW:
                return COLOR_YELLOW;
            case ChartObject.Color.BLUE:
                return COLOR_BLUE;
            case ChartObject.Color.PINK:
                return COLOR_PINK;
            case ChartObject.Color.PURPLE:
                return COLOR_NAVY;
        }

        return COLOR_GREEN;
    }
}
