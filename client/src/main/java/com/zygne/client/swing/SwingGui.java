package com.zygne.client.swing;

import com.zygne.client.ProjectProps;
import com.zygne.client.swing.components.tabs.CotTab;
import com.zygne.client.swing.components.tabs.ChartTab;
import com.zygne.client.swing.components.views.LoadingView;
import com.zygne.client.swing.components.UiLogger;
import com.zygne.client.swing.components.tabs.TendencyTab;
import com.zygne.data.domain.model.*;
import com.zygne.data.presentation.presenter.base.*;
import com.zygne.data.presentation.presenter.implementation.*;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.executor.ThreadExecutor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingGui extends JPanel implements MainPresenter.View,
        ChartTab.Callback,
        TendencyTab.Callback,
        TendencyPresenter.View,
        CotPresenter.View,
        BiasPresenter.View,
        RatioPresenter.View {

    private final JLabel labelStatus;
    private final JLabel labelLoading;
    private final LoadingView loadingView;

    private final MainPresenter mainPresenter;
    private final TendencyPresenter tendencyPresenter;
    private final CotPresenter cotPresenter;
    private final BiasPresenter biasPresenter;
    private final RatioPresenter ratioPresenter;

    private final ChartTab settingsTab;
    private final TendencyTab tendencyTab;
    private final CotTab cotTab;

    private final JTabbedPane tabbedPane;

    public SwingGui() {
        super(new BorderLayout());
        setSize(880, 880);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        settingsTab = new ChartTab(this);
        tendencyTab = new TendencyTab(this);
        cotTab = new CotTab();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Settings", settingsTab);
        tabbedPane.addTab("Settings", settingsTab);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;

        JPanel loadingPanel = new JPanel(new BorderLayout());

        labelLoading = new JLabel("");

        loadingPanel.add(labelLoading, BorderLayout.NORTH);
        loadingView = new LoadingView();
        loadingPanel.add(loadingView, BorderLayout.CENTER);

        constraints.gridy = 1;
        infoPanel.add(loadingView, constraints);

        add(infoPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());

        labelStatus = new JLabel("");
        statusPanel.add(labelStatus, BorderLayout.WEST);
        statusPanel.add(new JLabel(ProjectProps.readProperty("version")), BorderLayout.EAST);
        add(statusPanel, BorderLayout.SOUTH);

        Logger logger = new UiLogger(new JavaSwingThread(), labelStatus);
        logger.setUp();

        MainThread mainThread = new JavaSwingThread();
        Executor executor = ThreadExecutor.getInstance();
        tendencyPresenter = new TendencyPresenterImpl(executor, mainThread, this, logger);
        mainPresenter = new MainPresenterImpl(executor, mainThread, this, logger);
        cotPresenter = new CotPresenterImpl(executor, mainThread, this, logger);
        biasPresenter = new BiasPresenterImpl(executor, mainThread, this, logger);
        ratioPresenter = new RatioPresenterImpl(executor, mainThread, this);
    }

    @Override
    public void onComplete(List<Histogram> histograms, String symbol, String dateRange) {
        labelStatus.setText("");
        labelLoading.setText("");
        settingsTab.priceChartView.addData(histograms, symbol.toUpperCase());
        ratioPresenter.createRatio(histograms);
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
    public void generateReport(String symbol) {
        if (mainPresenter != null) {
            mainPresenter.createReport(symbol);
        }
    }

    @Override
    public void prepareView() {
        tabbedPane.removeAll();
        tabbedPane.addTab("Price Chart", settingsTab);
        tabbedPane.addTab("Seasonality Chart", tendencyTab);
        tabbedPane.addTab("COT", cotTab);
    }

    @Override
    public void onTendencyAssetsPrepared(List<Asset> assets, int defaultSelection) {
        tendencyTab.setAssets(assets);
    }

    @Override
    public void generateTendency(String symbol) {
        tendencyPresenter.createTendency(symbol);
        cotPresenter.createReport(symbol);
        biasPresenter.createBias(symbol);
    }

    @Override
    public void onTendencyReportCreated(List<Histogram> raw, TendencyReport tendencyReport) {
        tendencyTab.addTendency(tendencyReport);
        ratioPresenter.createRatio(raw);
    }

    @Override
    public void onCotDataReady(List<CotData> cotData) {
        cotTab.setCotData(cotData);
    }

    @Override
    public void onBiasCreated(List<Bias> biasList) {
    }

    @Override
    public void onRatioCreated(Ratio ratio) {
    }

    public void launch() {
        JFrame frame = new JFrame(ProjectProps.readProperty("name"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SwingGui());
        frame.getContentPane().setPreferredSize(new Dimension(1024, 512));
        frame.pack();
        frame.setVisible(true);
    }
}
