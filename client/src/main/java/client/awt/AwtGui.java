package client.awt;

import client.Constants;
import client.awt.components.LoadingView;
import client.awt.components.tabs.*;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.executor.ThreadExecutor;
import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.TimeFrame;
import com.zygne.stockanalyzer.presentation.presenter.base.LiquiditySidePresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.ScriptPresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.SettingsPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.LiquiditySidePresenterImpl;
import com.zygne.stockanalyzer.presentation.presenter.implementation.MainPresenterImpl;
import com.zygne.stockanalyzer.presentation.presenter.implementation.ScriptPresenterImpl;
import com.zygne.stockanalyzer.presentation.presenter.implementation.SettingsPresenterImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class AwtGui extends JPanel implements MainPresenter.View,
        LiquiditySidePresenter.View,
        ScriptPresenter.View,
        SettingsPresenter.View,
        MainTab.Callback,
        LiquiditySideTab.Callback,
        ScriptTab.Callback {

    private JLabel labelSymbol;
    private JLabel labelStatus;
    private JLabel labelLoading;
    private LoadingView loadingView;

    private String symbol;

    private MainPresenter mainPresenter;
    private LiquiditySidePresenter liquiditySidePresenter;
    private ScriptPresenter scriptPresenter;
    private MainThread mainThread = new JavaAwtThread();
    private Executor executor = ThreadExecutor.getInstance();

    private PriceAnalysisTab priceAnalysisTab;
    private MainTab mainTab;
    private LiquiditySideTab liquiditySideTab;
    private FundamentalsTab fundamentalsTab;
    private ScriptTab scriptTab;

    public AwtGui() {
        super(new BorderLayout());
        setSize(880, 880);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        priceAnalysisTab = new PriceAnalysisTab();
        mainTab = new MainTab();
        mainTab.setCallback(this);
        liquiditySideTab = new LiquiditySideTab();
        liquiditySideTab.setCallback(this);
        fundamentalsTab = new FundamentalsTab();
        scriptTab = new ScriptTab();
        scriptTab.setCallback(this);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Main", mainTab);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        tabbedPane.addTab("Price Analysis", priceAnalysisTab);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        tabbedPane.addTab("Buy / Sell Side", liquiditySideTab);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_4);

        tabbedPane.addTab("Script", scriptTab);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_5);

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

        add(statusPanel, BorderLayout.SOUTH);

        new SettingsPresenterImpl(executor, mainThread, this).start();
    }

    @Override
    public void onLiquiditySidesGenerated(List<LiquiditySide> data) {
        liquiditySideTab.addSides(data);
        scriptPresenter.setZones(data);
    }

    @Override
    public void onResistanceFound(List<LiquidityLevel> zones) {
        if (priceAnalysisTab != null) {
            priceAnalysisTab.addResistance(zones);
        }

        scriptPresenter.setResistance(zones);
    }

    @Override
    public void onSupportFound(List<LiquidityLevel> zones) {
        if (priceAnalysisTab != null) {
            priceAnalysisTab.addSupport(zones);
        }
    }

    @Override
    public void onPivotFound(List<LiquidityLevel> zones) {

    }

    @Override
    public void onFundamentalsLoaded(Fundamentals fundamentals) {

    }

    @Override
    public void onComplete(String symbol, String timeFrame) {
        this.symbol = symbol;
        labelSymbol.setText("Symbol : " + symbol.toUpperCase() + " " + timeFrame.toString());
        labelStatus.setText("");
        labelLoading.setText("");
        scriptPresenter.setSymbol(symbol);
    }

    @Override
    public void onTimeFramesPrepared(List<TimeFrame> timeFrames, int defaultSelection) {
        mainTab.setTimeFrames(timeFrames, defaultSelection);
        liquiditySideTab.setTimeFrames(timeFrames, defaultSelection);
    }

    @Override
    public void onDataSizePrepared(List<Integer> data, int defaultSelection) {
        mainTab.setDataSize(data, defaultSelection);
    }

    @Override
    public void onStatusUpdate(String status) {
        labelStatus.setText(status);
    }

    @Override
    public void onScriptCreated(String script) {
        scriptTab.addScript(script);
    }

    @Override
    public void onSettingsLoaded(Settings settings) {
        if (mainTab != null) {
            mainTab.setApiKey(settings.getApiKey());
        }
        mainPresenter = new MainPresenterImpl(executor, mainThread, this, settings);
        liquiditySidePresenter = new LiquiditySidePresenterImpl(executor, mainThread, this, settings);
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
    public void generateReport(String symbol, double percentile, TimeFrame timeFrame, int dataSize, boolean fundamentals) {
        if (mainPresenter != null) {
            mainPresenter.getZones(symbol, percentile, timeFrame, dataSize, fundamentals);
        }
    }

    @Override
    public void findLiquiditySides(TimeFrame timeFrame, double size, double percentile, double price) {
        if (liquiditySidePresenter != null) {
            liquiditySidePresenter.getSides(symbol, timeFrame, size, percentile, price);
        }
    }

    @Override
    public void generateScript(boolean resistance, boolean sides) {
        if (scriptPresenter != null) {
            scriptPresenter.createScript(resistance, sides);
        }
    }

    public void launch(String args[]) {
        JFrame frame = new JFrame(Constants.APP_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new AwtGui());

        frame.pack();
        frame.setVisible(true);
    }
}
