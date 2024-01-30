package com.zygne.client.awt;

import com.zygne.client.Constants;
import com.zygne.client.awt.components.tabs.ChartTab;
import com.zygne.client.awt.components.tabs.SettingsTab;
import com.zygne.client.awt.components.views.LoadingView;
import com.zygne.client.awt.components.UiLogger;
import com.zygne.client.awt.components.tabs.TendencyTab;
import com.zygne.client.awt.components.tabs.VpaTab;
import com.zygne.data.domain.model.*;
import com.zygne.data.domain.model.enums.TimeInterval;
import com.zygne.data.presentation.presenter.base.*;
import com.zygne.data.presentation.presenter.implementation.*;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.executor.ThreadExecutor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AwtGui extends JPanel implements MainPresenter.View,
        SettingsTab.Callback,
        ChartPresenter.View,
        ChartTab.Callback,
        TendencyTab.Callback,
        TendencyPresenter.View {

    private final ResourceLoader resourceLoader = new ResourceLoader();

    private final JLabel labelSymbol;
    private final JLabel labelStatus;
    private final JLabel labelLoading;
    private final LoadingView loadingView;

    private String symbol;
    private DataSize dataSize;

    private final MainPresenter mainPresenter;
    private final ChartPresenter chartPresenter;
    private final TendencyPresenter tendencyPresenter;

    private final SettingsTab settingsTab;
    private final VpaTab vpaTab;
    private final ChartTab chartTab;
    private final TendencyTab tendencyTab;

    private final JTabbedPane tabbedPane;

    public AwtGui() {
        super(new BorderLayout());
        setSize(880, 880);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        settingsTab = new SettingsTab(this);
        vpaTab = new VpaTab();
        chartTab = new ChartTab(this);
        tendencyTab = new TendencyTab(this);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Settings", settingsTab);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        labelSymbol = new JLabel("Symbol");

        infoPanel.add(labelSymbol, constraints);

        JPanel loadingPanel = new JPanel(new BorderLayout());

        labelLoading = new JLabel("");

        loadingPanel.add(labelLoading, BorderLayout.NORTH);
        loadingView = new LoadingView();
        loadingPanel.add(loadingView, BorderLayout.CENTER);

        constraints.gridy = 1;
        constraints.gridx = 0;
        infoPanel.add(loadingView, constraints);

        add(infoPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());

        labelStatus = new JLabel("");
        statusPanel.add(labelStatus, BorderLayout.WEST);
        statusPanel.add(new JLabel(Constants.VERSION_NAME), BorderLayout.EAST);

        Logger logger = new UiLogger(new JavaAwtThread(), labelStatus);
        logger.setUp();

        MainThread mainThread = new JavaAwtThread();
        Executor executor = ThreadExecutor.getInstance();
        chartPresenter = new ChartPresenterImpl(executor, mainThread, this, logger);
        tendencyPresenter = new TendencyPresenterImpl(executor, mainThread, this, logger);
        mainPresenter = new MainPresenterImpl(executor, mainThread, this, logger);
    }

    @Override
    public void onSupplyCreated(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        vpaTab.addSupply(filtered);

        vpaTab.addVolumeProfile(symbol, raw);
        chartTab.addVolumeProfile(raw);
        chartPresenter.setSupply(filtered);

    }

    @Override
    public void onComplete(String symbol, String timeFrame, String dateRange) {
        this.symbol = symbol;
        labelSymbol.setText("Symbol : " + symbol.toUpperCase() + " " + timeFrame + " - " + dateRange);
        labelStatus.setText("");
        labelLoading.setText("");
        chartPresenter.getChartData(symbol, TimeInterval.Day, dataSize);
    }

    @Override
    public void onTimeFramesPrepared(List<TimeInterval> timeIntervals, int defaultSelection) {
        settingsTab.setTimeFrames(timeIntervals, defaultSelection);
    }

    @Override
    public void onDataSizePrepared(List<DataSize> data, int defaultSelection) {
        settingsTab.setDataSize(data, defaultSelection);
    }

    @Override
    public void showLoading(String message) {
        loadingView.showLoading(message);
    }

    @Override
    public void hideLoading() {
        loadingView.hideLoading();
    }

    @Override
    public void showError(String message) {
        labelStatus.setText(message);
    }

    @Override
    public void generateReport(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize) {
        if (mainPresenter != null) {
            this.symbol = symbol.toUpperCase();
            this.dataSize = dataSize;
            mainPresenter.createReport(symbol, percentile, timeInterval, dataSize);
        }
    }

    @Override
    public void prepareView() {
        tabbedPane.removeAll();

        tabbedPane.addTab("Settings", settingsTab);
        tabbedPane.addTab("VPA", vpaTab);
        tabbedPane.addTab("Tendencies", tendencyTab);
        tabbedPane.addTab("Chart", chartTab);
    }

    @Override
    public void onHistogramCreated(List<Histogram> histograms) {
        chartPresenter.setHistograms(histograms);
    }

    public void launch() {
        JFrame frame = new JFrame(Constants.APP_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new AwtGui());
        Image image = resourceLoader.loadImage("icon.png");

        if (image != null) {
            frame.setIconImage(image);
        }

        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onChartReady(List<Histogram> histograms, List<PriceGap> priceGaps, List<PriceImbalance> priceImbalances, List<LiquidityLevel> liquidityLevels) {
        chartTab.addData(histograms, symbol.toUpperCase());
        chartTab.addPriceGaps(priceGaps);
        chartTab.addPriceImbalances(priceImbalances);
        chartTab.addSupply(liquidityLevels);
    }

    @Override
    public void createChart() {

        if (symbol == null) {
            showError("No Symbol for chart!!");
            return;
        }
        if (symbol.isEmpty()) {
            showError("No Symbol for chart!!");
        }
    }

    @Override
    public void onTendencyAssetsPrepared(List<Asset> assets, int defaultSelection) {
        tendencyTab.setAssets(assets, defaultSelection);
    }

    @Override
    public void generateTendency(String symbol) {
        tendencyPresenter.createTendency(symbol);
    }

    @Override
    public void onTendencyReportCreated(TendencyReport tendencyReport) {
        tendencyTab.addTendency(tendencyReport);
    }
}
