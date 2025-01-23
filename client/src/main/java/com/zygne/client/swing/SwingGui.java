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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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

    public SwingGui(String avApi, String polygonApi) {
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

        mainPresenter = new MainPresenterImpl(executor, mainThread, this, logger, avApi, polygonApi);
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

    public static void launch(String avApi, String polygonApi) {
        EventQueue.invokeLater(() -> {

            int year = Calendar.getInstance().get(Calendar.YEAR);
            String title = ProjectProps.readProperty("name") + " by " + ProjectProps.readProperty("author") + " - " + year;
            JFrame frame = new JFrame(title);

            try {
                List<Image> icons = new ArrayList<>();
                InputStream stream = SwingGui.class
                        .getResourceAsStream( "/icon.png" );

                if(stream != null) {
                    BufferedImage image = ImageIO.read(stream);
                    if (image == null) {
                        System.out.println("Icon url is null");
                    } else {
                        icons.add(image);
                    }
                } else {
                    System.out.println("Error : Cannot open stream");
                }

                frame.setIconImages(icons);
            } catch (Exception e) {
                e.printStackTrace();
            }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SwingGui(avApi, polygonApi));
            frame.getContentPane().setPreferredSize(new Dimension(1024, 512));
            frame.pack();
            frame.setVisible(true);
        });

    }

    @Override
    public void reportButtonClicked(String symbol, int type) {
        if (mainPresenter != null) {
            mainPresenter.createReport(symbol, type);
        }
    }

    @Override
    public void configButtonClicked(Boolean adjust) {
        if (mainPresenter != null) {
            mainPresenter.adjust(adjust);
        }
    }
}
