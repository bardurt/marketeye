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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AwtGui extends JPanel implements MainPresenter.View,
        SettingsPresenter.View,
        ScriptTab.Callback,
        SettingsTab.Callback,
        LiquiditySideTab.Callback,
        ChartPresenter.View,
        ChartTab.Callback {

    private Settings settings;
    private ResourceLoader resourceLoader = new ResourceLoader();

    private JLabel labelSymbol;
    private JLabel labelStatus;
    private JLabel labelLoading;
    private LoadingView loadingView;

    private JButton jButtonConnect;

    private String symbol;
    private DataSize dataSize;
    private TimeInterval timeInterval;

    private SettingsPresenter settingsPresenter;
    private MainPresenter mainPresenter;
    private ChartPresenter chartPresenter;
    private MainThread mainThread = new JavaAwtThread();
    private Executor executor = ThreadExecutor.getInstance();

    private SettingsTab settingsTab;
    private VpaTab vpaTab;
    private LiquiditySideTab liquiditySideTab;
    private ScriptTab scriptTab;
    private VolumeTab volumeTab;
    private PnfTab pnfTab;
    private ChartTab chartTab;

    private Logger logger;

    private DataProvider dataProvider;


    private JTabbedPane tabbedPane = new JTabbedPane();

    public AwtGui() {
        super(new BorderLayout());
        setSize(880, 880);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        settingsTab = new SettingsTab(resourceLoader);
        settingsTab.setCallback(this);
        vpaTab = new VpaTab();
        liquiditySideTab = new LiquiditySideTab();
        liquiditySideTab.setCallback(this);
        scriptTab = new ScriptTab();
        scriptTab.setCallback(this);
        volumeTab = new VolumeTab();
        pnfTab = new PnfTab();
        chartTab = new ChartTab(this);

        tabbedPane = new JTabbedPane();
        Font font = new Font("Verdana", Font.CENTER_BASELINE, 14);
        //tabbedPane.setFont(font);

        tabbedPane.addTab("\"<html><h1 style='padding:20px;'>Settings</h1></html>\"", settingsTab);

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

        logger = new UiLogger(new JavaAwtThread(), settingsTab.getLogArea());
        logger.setUp();

        chartPresenter = new ChartPresenterImpl(executor, mainThread, this, logger);
        settingsPresenter = new SettingsPresenterImpl(executor, mainThread, this);
        settingsPresenter.start();
    }

    @Override
    public void onSupplyCreated(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        vpaTab.addSupply(filtered);
        liquiditySideTab.clear();

        vpaTab.addVolumeProfile(symbol, raw);
        chartTab.addVolumeProfile(raw);
        chartPresenter.setSupply(filtered);

    }

    @Override
    public void onComplete(String symbol, String timeFrame, String dateRange) {
        this.symbol = symbol;
        labelSymbol.setText("Symbol : " + symbol.toUpperCase() + " " + timeFrame.toString() + " - " + dateRange);
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
    public void onSettingsLoaded(Settings settings) {
        this.settings = settings;
        this.dataProvider = settings.getDataProvider();
        if (settingsTab != null) {
            settingsTab.setSettings(settings);
        }


        mainPresenter = new MainPresenterImpl(executor, mainThread, this, settings, logger);
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
            this.dataSize = dataSize;
            this.timeInterval = timeInterval;
            mainPresenter.setAsset(asset);
            mainPresenter.createReport(symbol, percentile, timeInterval, dataSize, fundamentals, cache);
        }
    }

    @Override
    public void fetchLastPrice(String symbol) {
    }

    @Override
    public void generateScript(boolean resistance, boolean sides, boolean gaps) {
    }

    @Override
    public void onIntraDayPriceGapsFound(List<PriceGap> data) {
    }

    private void connect() {
        if (mainPresenter != null) {
            mainPresenter.toggleConnection();
        }
    }

    @Override
    public void onProviderSelected(DataProvider dataProvider) {

        this.dataProvider = dataProvider;
        if (dataProvider == DataProvider.CRYPTO_COMPARE) {
            chartPresenter.setAsset(DataBroker.Asset.Crypto);
        } else {
            chartPresenter.setAsset(DataBroker.Asset.Stock);
        }

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
    public void prepareView(List<MainPresenter.ViewComponent> viewComponents) {
        tabbedPane.removeAll();

        tabbedPane.addTab("<html><h3 style='padding:20px;'>Settings</h3></html>", settingsTab);

        for (MainPresenter.ViewComponent v : viewComponents) {

            if (v == MainPresenter.ViewComponent.VPA) {
                tabbedPane.addTab("<html><h3 style='padding:20px;'>VPA</h3></html>", vpaTab);
                //tabbedPane.addTab("Highest Volume", volumeTab);
                //tabbedPane.addTab("High Vol Bars", highVolumeBarTab);
            }

            if (v == MainPresenter.ViewComponent.WICKS) {
                tabbedPane.addTab("<html><h3 style='padding:20px;'>Chart</h3></html>", chartTab);
            }

        }
    }

    @Override
    public void onDailyBarsCreated(List<Histogram> histograms) {
        pnfTab.addVolumeProfile("", histograms);

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
    public void onLatestPriceFetched(double price) {

        String text = labelSymbol.getText();
        text += " - Price : " + String.format("%.2f", price);
        labelSymbol.setText(text);
        vpaTab.setCurrentPrice(price);
    }

    @Override
    public void findLiquidity() {
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
            return;
        }
    }
}
