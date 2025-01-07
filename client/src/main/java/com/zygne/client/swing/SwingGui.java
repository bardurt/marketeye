package com.zygne.client.swing;

import com.zygne.client.ProjectProps;
import com.zygne.client.swing.components.tabs.ChartTab;
import com.zygne.client.swing.components.tabs.WeeklyChartTab;
import com.zygne.client.swing.components.views.LoadingView;
import com.zygne.client.swing.components.UiLogger;
import com.zygne.client.swing.components.tabs.TendencyTab;
import com.zygne.client.swing.components.views.StocksView;
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
        StocksView.Callback {

    private final StocksView reportView;

    private final JLabel labelStatus;
    private final JLabel labelLoading;
    private final LoadingView loadingView;

    private final MainPresenter mainPresenter;

    private final ChartTab settingsTab;
    private final TendencyTab tendencyTab;
    private final WeeklyChartTab weeklyChartTab;

    private final JTabbedPane tabbedPane;

    private String symbol = "";

    public SwingGui() {
        super(new BorderLayout());
        setSize(880, 880);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        reportView = new StocksView();
        reportView.setCallback(this);

        settingsTab = new ChartTab();
        tendencyTab = new TendencyTab();
        weeklyChartTab = new WeeklyChartTab();

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
        String api = ProjectProps.readProperty("api");
        mainPresenter = new MainPresenterImpl(executor, mainThread, this, logger, api);
    }


    @Override
    public void onComplete(List<Histogram> daily, List<Histogram> weekly, TendencyReport tendencyReport, String symbol) {
        this.symbol = symbol;
        labelStatus.setText("");
        labelLoading.setText("");
        settingsTab.priceChartView.addData(daily, symbol.toUpperCase() + " Daily");
        weeklyChartTab.priceChartView.addData(weekly, symbol.toUpperCase() + " Weekly");
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
        tabbedPane.addTab("Daily", settingsTab);
        tabbedPane.addTab("Weekly", weeklyChartTab);
        tabbedPane.addTab("Seasonality", tendencyTab);
    }

    public void launch() {
        JFrame frame = new JFrame(ProjectProps.readProperty("name"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SwingGui());
        frame.getContentPane().setPreferredSize(new Dimension(1024, 512));
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void reportButtonClicked(String symbol) {
        if (mainPresenter != null) {
            mainPresenter.createReport(symbol);
        }
    }
}
