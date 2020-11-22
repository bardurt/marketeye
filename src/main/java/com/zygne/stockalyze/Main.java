package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.executor.ThreadExecutor;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;
import com.zygne.stockalyze.domain.utils.TimeHelper;
import com.zygne.stockalyze.presentation.presenter.base.LiquiditySidePresenter;
import com.zygne.stockalyze.presentation.presenter.base.MainPresenter;
import com.zygne.stockalyze.presentation.presenter.base.ScriptPresenter;
import com.zygne.stockalyze.presentation.presenter.base.SettingsPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.LiquiditySidePresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.MainPresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.ScriptPresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.SettingsPresenterImpl;
import com.zygne.stockalyze.domain.utils.ColorHelper;
import com.zygne.stockalyze.domain.utils.StringUtils;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements MainPresenter.View,
        LiquiditySidePresenter.View,
        ScriptPresenter.View,
        SettingsPresenter.View {

    private final ResourceLoader resourceLoader = new ResourceLoader();

    private TextField tfTicker;
    private TextField tfTopPercentFilter;
    private TextField tfZonesTopPercent;
    private TextArea taScript;

    private RadioButton rbFundamentals;
    private RadioButton rbSidesScript;
    private RadioButton rbResistanceScript;

    private Label labelLoading;
    private ProgressIndicator piLoading;
    private Label labelError;

    private ChoiceBox cbPowerZoneTimeFrame;
    private ChoiceBox cbReportTimeFrame;

    private TableView tableViewResistance;
    private TableView tableViewSupport;
    private TableView tableViewPivot;
    private TableView tableViewPowerZones;
    private TableView tableFundamentals;

    private Label labelTicker;

    private String ticker;
    private MainPresenter mainPresenter;
    private LiquiditySidePresenter liquiditySidePresenter;
    private ScriptPresenter scriptPresenter;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnCloseRequest(t -> ThreadExecutor.getInstance().stop());

        resourceLoader.setContent("layout_main");
        resourceLoader.setStyle("style");
        resourceLoader.inflate(primaryStage, Constants.APP_NAME);

        tfTicker = (TextField) resourceLoader.findView("tf_ticker");
        tfTopPercentFilter = (TextField) resourceLoader.findView("tf_percent_filter");
        tfTopPercentFilter.setText("90");

        tfZonesTopPercent = (TextField) resourceLoader.findView("tf_percent_filter_zones");
        tfZonesTopPercent.setText("90");

        taScript = (TextArea) resourceLoader.findView("ta_script");

        labelTicker = (Label) resourceLoader.findView("label_ticker");
        labelError = (Label) resourceLoader.findView("label_error");
        labelLoading = (Label) resourceLoader.findView("label_loading");
        piLoading = (ProgressIndicator) resourceLoader.findView("pi_loading");
        Label labelVersion = (Label) resourceLoader.findView("label_version");
        labelVersion.setText(Constants.VERSION_NAME);

        tableViewResistance = (TableView) resourceLoader.findView("table_supply_zones");
        tableViewSupport = (TableView) resourceLoader.findView("table_support");
        tableViewPivot = (TableView) resourceLoader.findView("table_pivot");
        tableViewPowerZones = (TableView) resourceLoader.findView("table_power_zones");
        tableFundamentals = (TableView) resourceLoader.findView("table_fundamentals");

        Button btnCreateReport = (Button) resourceLoader.findView("btn_create_report");
        btnCreateReport.setOnAction((ActionEvent event) -> createReport());

        Button btnPowerZones = (Button) resourceLoader.findView("btn_power_zones");
        btnPowerZones.setOnAction((ActionEvent event) -> createPowerZones());

        Button btnCreateScript = (Button) resourceLoader.findView("btn_create_script");
        btnCreateScript.setOnAction((ActionEvent event) -> createScript());

        rbFundamentals = (RadioButton) resourceLoader.findView("rb_fundamentals");
        rbResistanceScript = (RadioButton) resourceLoader.findView("rb_script_resistance");
        rbSidesScript = (RadioButton) resourceLoader.findView("rb_script_sides");

        cbPowerZoneTimeFrame = (ChoiceBox) resourceLoader.findView("cb_power_zone_time_frame");

        ObservableList<TimeFrame> timeFrame = FXCollections.observableArrayList();
        timeFrame.add(TimeFrame.Fifteen_Minutes);
        timeFrame.add(TimeFrame.Thirty_Minutes);
        timeFrame.add(TimeFrame.Hour);
        timeFrame.add(TimeFrame.Day);

        cbPowerZoneTimeFrame.setItems(timeFrame);
        cbPowerZoneTimeFrame.setValue(timeFrame.get(0));

        cbReportTimeFrame = (ChoiceBox) resourceLoader.findView("cb_report_time_frame");

        prepareResistanceTable();
        prepareSupportTable();
        preparePivotTable();
        prepareTableFundamentals();
        preparePowerZoneTable();

        SettingsPresenter settingsPresenter = new SettingsPresenterImpl(ThreadExecutor.getInstance(), new JavaFxThread(), this);
        settingsPresenter.start();
    }


    private void populateResistanceZones(List<LiquidityLevel> data) {
        tableViewResistance.getItems().clear();
        tableViewResistance.getItems().addAll(data);
    }

    private void populateSupportTable(List<LiquidityLevel> data) {
        tableViewSupport.getItems().clear();
        tableViewSupport.getItems().addAll(data);
    }

    private void populatePivotTable(List<LiquidityLevel> data) {
        tableViewPivot.getItems().clear();
        tableViewPivot.getItems().addAll(data);
    }

    private void populateFundamentals(Fundamentals fundamentals) {
        tableFundamentals.getItems().clear();

        if (fundamentals != null) {
            tableFundamentals.getItems().add(fundamentals);
        }
        tableFundamentals.setFixedCellSize(45);
        tableFundamentals.prefHeightProperty().bind(Bindings.size(tableFundamentals.getItems()).multiply(tableFundamentals.getFixedCellSize()).add(45));

    }

    private void populatePowerZone(List<LiquiditySide> data) {
        tableViewPowerZones.getItems().clear();
        tableViewPowerZones.getItems().addAll(data);
    }

    private void prepareResistanceTable() {
        tableViewResistance.setPlaceholder(new Label());

        TableColumn headerPrice = new TableColumn("Price");
        headerPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        headerPrice.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        TableColumn headerVol = new TableColumn("Vol");
        headerVol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        headerVol.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,d", value.intValue()));
                }
            }
        });


        TableColumn headerPercentile = new TableColumn("Pctl");
        headerPercentile.setCellValueFactory(new PropertyValueFactory<>("percentile"));
        headerPercentile.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        TableColumn headerBreakPoint = new TableColumn("Break");
        headerBreakPoint.setCellValueFactory(new PropertyValueFactory<>("breakPoint"));
        headerBreakPoint.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", value.doubleValue()));
                }
            }
        });

        tableViewResistance.getColumns().addAll(headerPrice, headerVol, headerPercentile, headerBreakPoint);

        tableViewResistance.setRowFactory(tv -> new TableRow<LiquidityLevel>() {
            @Override
            protected void updateItem(LiquidityLevel item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    String color = ColorHelper.getColorForPercentile(item.getPercentile());

                    if (color != null) {
                        setStyle("-fx-background-color: -fx-table-cell-border-color, " + color + ";");
                    } else {
                        setStyle("");
                    }

                } else {
                    setStyle("");
                }
            }
        });
    }

    private void preparePivotTable() {
        tableViewPivot.setPlaceholder(new Label());

        TableColumn headerPrice = new TableColumn("Price");
        headerPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        headerPrice.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        tableViewPivot.getColumns().addAll(headerPrice);
    }

    private void prepareSupportTable() {
        tableViewSupport.setPlaceholder(new Label());

        TableColumn headerPrice = new TableColumn("Price");
        headerPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        headerPrice.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        TableColumn headerVol = new TableColumn("Vol");
        headerVol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        headerVol.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,d", value.intValue()));
                }
            }
        });


        TableColumn headerPercentile = new TableColumn("Pctl");
        headerPercentile.setCellValueFactory(new PropertyValueFactory<>("percentile"));
        headerPercentile.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        TableColumn headerBreakPoint = new TableColumn("Break");
        headerBreakPoint.setCellValueFactory(new PropertyValueFactory<>("breakPoint"));
        headerBreakPoint.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", value.doubleValue()));
                }
            }
        });

        tableViewSupport.getColumns().addAll(headerPrice, headerVol, headerPercentile, headerBreakPoint);

        tableViewSupport.setRowFactory(tv -> new TableRow<LiquidityLevel>() {
            @Override
            protected void updateItem(LiquidityLevel item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {

                    String color = ColorHelper.getColorForPercentile(item.getPercentile());

                    if (color != null) {
                        setStyle("-fx-background-color: -fx-table-cell-border-color, " + color + ";");
                    } else {
                        setStyle("");
                    }

                } else {
                    setStyle("");
                }
            }
        });
    }

    private void prepareTableFundamentals() {
        tableFundamentals.setPlaceholder(new Label());

        TableColumn headerFloat = new TableColumn("Float");
        headerFloat.setCellValueFactory(new PropertyValueFactory<>("sharesFloat"));
        headerFloat.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    if (value != null) {
                        setText(String.format("%,d", value.intValue()));
                    }
                }
            }
        });

        TableColumn headerHigh = new TableColumn("52w High");
        headerHigh.setCellValueFactory(new PropertyValueFactory<>("high"));

        TableColumn headerLow = new TableColumn("52w Low");
        headerLow.setCellValueFactory(new PropertyValueFactory<>("low"));

        tableFundamentals.getColumns().addAll(headerFloat, headerHigh, headerLow);

    }

    private void preparePowerZoneTable() {
        TableColumn headerStart = new TableColumn("Start");
        headerStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        headerStart.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });


        TableColumn headerEnd = new TableColumn("End");
        headerEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        headerEnd.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        TableColumn headerSide = new TableColumn("Side");
        headerSide.setCellValueFactory(new PropertyValueFactory<>("side"));

        TableColumn headerVolume = new TableColumn("Vol");
        headerVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        headerVolume.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,d", value.intValue()));
                }
            }
        });

        TableColumn headerVolumeRank = new TableColumn("Rank");
        headerVolumeRank.setCellValueFactory(new PropertyValueFactory<>("volumeRank"));

        TableColumn headerDay = new TableColumn("Time");
        headerDay.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        headerDay.setCellFactory(tc -> new TableCell<LiquidityLevel, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(TimeHelper.getDateFromTimeStamp(value.longValue()));
                }
            }
        });

        tableViewPowerZones.getColumns().addAll(headerStart, headerEnd, headerVolume, headerVolumeRank, headerSide, headerDay);

        tableViewPowerZones.setRowFactory(tv -> new TableRow<LiquiditySide>() {
            @Override
            protected void updateItem(LiquiditySide item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {

                    String color = "#9E0000";

                    if (item.type == LiquiditySide.ACCEPT) {
                        color = "#079E00";
                    }

                    setStyle("-fx-background-color: -fx-table-cell-border-color, " + color + ";");

                } else {
                    setStyle("");
                }
            }
        });
    }

    private void createReport() {

        if (mainPresenter == null) {
            return;
        }

        clearTables();

        ticker = tfTicker.getText();

        String percent = tfTopPercentFilter.getText();

        double percentile = 0d;

        if (StringUtils.idDouble(percent)) {
            percentile = Double.parseDouble(percent);
        }

        TimeFrame timeFrame = (TimeFrame) cbReportTimeFrame.getSelectionModel().getSelectedItem();

        mainPresenter.getZones(ticker, percentile, timeFrame, rbFundamentals.isSelected());
    }

    private void createPowerZones() {
        if (liquiditySidePresenter == null) {
            return;
        }

        ticker = tfTicker.getText();

        String percent = tfZonesTopPercent.getText();

        double percentile = 0d;

        if (StringUtils.idDouble(percent)) {
            percentile = Double.parseDouble(percent);
        }

        if (!ticker.isEmpty()) {
            TimeFrame timeFrame = ((TimeFrame) (cbPowerZoneTimeFrame.getSelectionModel().getSelectedItem()));
            liquiditySidePresenter.getSides(ticker, timeFrame, percentile);
        }
    }

    private void createScript() {
        if (scriptPresenter == null) {
            return;
        }

        boolean resistance = rbResistanceScript.isSelected();
        boolean sides = rbSidesScript.isSelected();

        scriptPresenter.createScript(resistance, sides);
    }

    @Override
    public void showLoading(String message) {

        if (labelLoading != null) {
            labelLoading.setText(message);
            labelLoading.setVisible(true);
        }
        if (piLoading != null) {
            piLoading.setVisible(true);
        }

    }

    @Override
    public void hideLoading() {

        if (labelLoading != null) {
            labelLoading.setText("");
            labelLoading.setVisible(false);
        }
        if (piLoading != null) {
            piLoading.setVisible(false);
        }
    }

    @Override
    public void showError(String message) {
        if (labelError != null) {
            labelError.setText(message);
        }
    }

    @Override
    public void onResistanceFound(List<LiquidityLevel> zones) {
        populateResistanceZones(zones);
        scriptPresenter.setResistance(zones);
    }

    @Override
    public void onSupportFound(List<LiquidityLevel> zones) {
        populateSupportTable(zones);
    }

    @Override
    public void onPivotFound(List<LiquidityLevel> zones) {
        populatePivotTable(zones);
    }

    @Override
    public void onLiquiditySidesGenerated(List<LiquiditySide> data) {
        populatePowerZone(data);
        scriptPresenter.setZones(data);
    }

    @Override
    public void onFundamentalsLoaded(Fundamentals fundamentals) {
        populateFundamentals(fundamentals);
    }

    @Override
    public void onComplete(String symbol) {
        labelTicker.setText("Symbol : " + symbol.toUpperCase());
        scriptPresenter.setSymbol(symbol);
    }

    private void clearTables() {
        populateResistanceZones(new ArrayList<>());
        populateSupportTable(new ArrayList<>());
        populatePivotTable(new ArrayList<>());
        populatePowerZone(new ArrayList<>());
        populateFundamentals(null);
        taScript.setText("");
    }

    @Override
    public void onTimeFramesPrepared(List<TimeFrame> timeFrames, int defaultselection) {
        ObservableList<TimeFrame> reportTimeFrame = FXCollections.observableArrayList();

        reportTimeFrame.addAll(timeFrames);

        if (!reportTimeFrame.isEmpty()) {
            cbReportTimeFrame.setItems(reportTimeFrame);
            cbReportTimeFrame.setValue(reportTimeFrame.get(defaultselection));
        }
    }

    @Override
    public void onScriptCreated(String script) {
        taScript.setText(script);
    }

    @Override
    public void onSettingsLoaded(Settings settings) {
        mainPresenter = new MainPresenterImpl(ThreadExecutor.getInstance(), new JavaFxThread(), this, settings);
        liquiditySidePresenter = new LiquiditySidePresenterImpl(ThreadExecutor.getInstance(), new JavaFxThread(), this, settings);
        scriptPresenter = new ScriptPresenterImpl(ThreadExecutor.getInstance(), new JavaFxThread(), this);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
