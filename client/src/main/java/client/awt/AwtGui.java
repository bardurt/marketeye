package client.awt;

import client.Constants;
import client.awt.components.LoadingView;
import client.awt.components.UiLogger;
import client.awt.components.tabs.*;
import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.executor.ThreadExecutor;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.*;
import com.zygne.stockanalyzer.presentation.presenter.implementation.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AwtGui extends JPanel implements MainPresenter.View,
        ScriptPresenter.View,
        SettingsPresenter.View,
        ScriptTab.Callback,
        SettingsTab.Callback {

    private Settings settings;
    private ResourceLoader resourceLoader = new ResourceLoader();

    private JLabel labelSymbol;
    private JLabel labelStatus;
    private JLabel labelLoading;
    private LoadingView loadingView;

    private JButton jButtonConnect;

    private String symbol;

    private SettingsPresenter settingsPresenter;
    private MainPresenter mainPresenter;
    private ScriptPresenter scriptPresenter;
    private MainThread mainThread = new JavaAwtThread();
    private Executor executor = ThreadExecutor.getInstance();

    private SettingsTab settingsTab;
    private VpaTab vpaTab;
    private LiquiditySideTab liquiditySideTab;
    private ScriptTab scriptTab;
    private VolumeTab volumeTab;
    private PnfTab pnfTab;

    private JTabbedPane tabbedPane = new JTabbedPane();

    public AwtGui() {
        super(new BorderLayout());
        setSize(880, 880);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        settingsTab = new SettingsTab(resourceLoader);
        settingsTab.setCallback(this);
        vpaTab = new VpaTab();
        liquiditySideTab = new LiquiditySideTab();
        scriptTab = new ScriptTab();
        scriptTab.setCallback(this);
        volumeTab = new VolumeTab();
        pnfTab = new PnfTab();

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

        jButtonConnect = new JButton("Connect");
        jButtonConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        statusPanel.add(jButtonConnect, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.SOUTH);

        settingsPresenter = new SettingsPresenterImpl(executor, mainThread, this);
        settingsPresenter.start();
    }

    @Override
    public void onDailyLiquidityGenerated(List<LiquiditySide> data) {
        liquiditySideTab.addDaily(data);
        List<LiquiditySide> sides = new ArrayList<>();

        for (LiquiditySide e : data) {
            if (e.getSide().equalsIgnoreCase("Buy")) {
            } else {
                sides.add(e);
            }
        }


        scriptPresenter.setZones(sides);
    }

    @Override
    public void onWeeklyLiquidityGenerated(List<LiquiditySide> data) {
        liquiditySideTab.addWeekly(data);
    }

    @Override
    public void onSupplyCreated(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        vpaTab.addSupply(filtered);
        liquiditySideTab.clear();

        scriptPresenter.setResistance(filtered);

        Collections.sort(filtered, new LiquidityLevel.VolumeComparator());
        Collections.reverse(filtered);

        List<LiquidityLevel> pocs = new ArrayList<>();

        for (LiquidityLevel e : filtered) {
            if (e.getPercentile() > 98) {
                pocs.add(e);
            }
        }

        vpaTab.addVolumeProfile(symbol, raw);
    }

    @Override
    public void onBinnedSupplyCreated(List<LiquidityLevel> zones) {
        vpaTab.addBinnedSupply(zones);
    }

    @Override
    public void onFundamentalsLoaded(Fundamentals fundamentals) {
        vpaTab.addFundamentals(fundamentals);
    }

    @Override
    public void onComplete(String symbol, String timeFrame, String dateRange) {
        this.symbol = symbol;
        labelSymbol.setText("Symbol : " + symbol.toUpperCase() + " " + timeFrame.toString() + " - " + dateRange);
        labelStatus.setText("");
        labelLoading.setText("");
        scriptPresenter.setSymbol(symbol);
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
    public void onStatusUpdate(String status) {
        labelStatus.setText(status);
    }

    @Override
    public void onConnected() {
        jButtonConnect.setText("Disconnect");
    }

    @Override
    public void onDisconnected() {
        jButtonConnect.setText("Connect");
    }

    @Override
    public void onScriptCreated(String script) {
        scriptTab.addScript(script);
    }

    @Override
    public void onSettingsLoaded(Settings settings) {
        this.settings = settings;
        if (settingsTab != null) {
            settingsTab.setSettings(settings);
        }

        Logger logger = new UiLogger(new JavaAwtThread(), settingsTab.getLogArea());
        logger.setUp();
        mainPresenter = new MainPresenterImpl(executor, mainThread, this, settings, logger);
        scriptPresenter = new ScriptPresenterImpl(executor, mainThread, this);
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
    public void generateReport(String symbol, double percentile, TimeInterval timeInterval, DataSize dataSize, boolean fundamentals, boolean cache, DataBroker.Asset asset) {
        if (mainPresenter != null) {
            this.symbol = symbol.toUpperCase();
            mainPresenter.setAsset(asset);
            mainPresenter.createReport(symbol, percentile, timeInterval, dataSize, fundamentals, cache);
        }
    }

    @Override
    public void generateScript(boolean resistance, boolean sides, boolean gaps) {
        if (scriptPresenter != null) {
            scriptPresenter.createScript(resistance, sides, gaps);
        }
    }


    @Override
    public void onDailyPriceGapsFound(List<PriceGap> data) {
        liquiditySideTab.addDailyPriceGaps(data);
        scriptPresenter.setGaps(data);
    }

    @Override
    public void onIntraDayPriceGapsFound(List<PriceGap> data) {
        liquiditySideTab.addIntraDayPriceGaps(data);
    }

    private void connect() {
        if (mainPresenter != null) {
            mainPresenter.toggleConnection();
        }
    }

    @Override
    public void onProviderSelected(DataProvider dataProvider) {
        settingsPresenter.loadSettings(dataProvider);
    }

    @Override
    public void toggleConnectionSettings(boolean b) {
        if (b) {
            jButtonConnect.setVisible(true);
        } else {
            jButtonConnect.setVisible(false);
        }
    }


    @Override
    public void onHighVolumeBarFound(List<VolumeBarDetails> data) {
        volumeTab.addHighVolBars(data);
    }

    @Override
    public void prepareView(List<MainPresenter.ViewComponent> viewComponents) {
        tabbedPane.removeAll();

        tabbedPane.addTab("Settings", settingsTab);

        for (MainPresenter.ViewComponent v : viewComponents) {

            if (v == MainPresenter.ViewComponent.VPA) {
                tabbedPane.addTab("VPA", vpaTab);
                tabbedPane.addTab("Highest Volume", volumeTab);
                //tabbedPane.addTab("High Vol Bars", highVolumeBarTab);
            }

            if (v == MainPresenter.ViewComponent.WICKS) {
                tabbedPane.addTab("Liquidity", liquiditySideTab);
            }

        }

        //tabbedPane.addTab("PnF", pnfTab);
    }

    @Override
    public void onDailyBarsCreated(List<Histogram> histograms) {
        pnfTab.addVolumeProfile("", histograms);
    }

    @Override
    public void onHistogramCreated(List<Histogram> histograms) {
    }

    public void launch() {
        JFrame frame = new JFrame(Constants.APP_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new AwtGui());

        Image image = resourceLoader.loadImage("icon.png");

        if(image != null) {
            frame.setIconImage(image);
        }

        frame.pack();
        frame.setVisible(true);
    }
}
