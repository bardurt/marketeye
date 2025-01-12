package com.zygne.client.swing;

import com.zygne.client.ProjectProps;
import com.zygne.client.swing.components.tabs.ChartTab;
import com.zygne.client.swing.components.views.LoadingView;
import com.zygne.client.swing.components.UiLogger;
import com.zygne.client.swing.components.tabs.TendencyTab;
import com.zygne.client.swing.components.views.SettingsView;
import com.zygne.data.domain.model.*;
import com.zygne.data.presentation.presenter.MainPresenter;
import com.zygne.data.presentation.presenter.MainPresenterImpl;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.executor.ThreadExecutor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingGui extends JPanel implements MainPresenter.View,
        SettingsView.Callback {

    private final SettingsView reportView;

    private final JLabel labelStatus;
    private final JLabel labelLoading;
    private final LoadingView loadingView;

    private final MainPresenter mainPresenter;

    private final ChartTab dailyChartTab;
    private final ChartTab weeklyChartTab;
    private final ChartTab moonthlyChartTab;
    private final TendencyTab tendencyTab;


    private final JTabbedPane tabbedPane;

    private String symbol = "";

    public SwingGui(String api) {
        super(new BorderLayout());
        setSize(880, 880);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        reportView = new SettingsView();
        reportView.setCallback(this);

        dailyChartTab = new ChartTab();
        weeklyChartTab = new ChartTab();
        moonthlyChartTab = new ChartTab();
        tendencyTab = new TendencyTab();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Settings", dailyChartTab);

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
        constraints.gridy = 2;
        infoPanel.add(reportView, constraints);

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

        mainPresenter = new MainPresenterImpl(executor, mainThread, this, logger, api);
    }


    @Override
    public void onComplete(List<Histogram> daily, List<Histogram> weekly, List<Histogram> monthly, TendencyReport tendencyReport, String symbol) {
        this.symbol = symbol;
        labelStatus.setText("");
        labelLoading.setText("");
        dailyChartTab.priceChartView.addData(daily, symbol.toUpperCase() + " Day");
        weeklyChartTab.priceChartView.addData(weekly, symbol.toUpperCase() + " Week");
        moonthlyChartTab.priceChartView.addData(monthly, symbol.toUpperCase() + " Month");
        tendencyTab.addTendency(tendencyReport, symbol.toUpperCase() + " Seasonality");
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
    public void prepareView() {
        tabbedPane.removeAll();
        tabbedPane.addTab("Daily", dailyChartTab);
        tabbedPane.addTab("Weekly", weeklyChartTab);
        tabbedPane.addTab("Monthly", moonthlyChartTab);
        tabbedPane.addTab("Seasonality", tendencyTab);
    }

    public static void launch(String api) {
        String title = ProjectProps.readProperty("name") + " by " + ProjectProps.readProperty("author");
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SwingGui(api));
        frame.getContentPane().setPreferredSize(new Dimension(1024, 512));
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void reportButtonClicked(String symbol, int type) {
        if (mainPresenter != null) {
            mainPresenter.createReport(symbol, type);
        }
    }
}
