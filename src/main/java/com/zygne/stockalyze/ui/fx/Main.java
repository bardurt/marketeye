package com.zygne.stockalyze.ui.fx;

import com.zygne.stockalyze.ResourceLoader;
import com.zygne.stockalyze.domain.executor.ThreadExecutor;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.presentation.presenter.base.DetailsPresenter;
import com.zygne.stockalyze.presentation.presenter.base.GapAnalysisPresenter;
import com.zygne.stockalyze.presentation.presenter.base.MainPresenter;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.GapAnalysisPresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.MainPresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.PredictionPresenterImpl;
import com.zygne.stockalyze.ui.uimodel.News;
import com.zygne.stockalyze.ui.uimodel.Trend;
import com.zygne.stockalyze.utils.ColorHelper;
import com.zygne.stockalyze.utils.StringUtils;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements MainPresenter.View,
        PredictionPresenter.View,
        GapAnalysisPresenter.View {

    private ResourceLoader resourceLoader = new ResourceLoader();

    private TextField tfTicker;
    private TextField tfTopPercentFilter;
    private TextField tfPrice;
    private TextField tfPredictionPrice;
    private TextArea taGapAnalysis;
    private TextField tfGapPercent;

    private Button btnCreateReport;
    private Button btnCreatePrediction;
    private Button btnFilter;
    private Button btnGapAnalysis;
    private Button btnCreateHeatMap;

    private RadioButton rbYahoo;
    private RadioButton rbAlphaVantage;

    private RadioButton vpaRuleOne;
    private RadioButton vpaRuleFour;
    private RadioButton vpaRuleFive;

    private Label labelLoading;
    private ProgressIndicator piLoading;
    private Label labelError;

    private ChoiceBox cbNews;
    private ChoiceBox cbTrend;

    private RadioButton rbGap;

    private TableView tableViewZones;
    private TableView tablePrediction;

    private Label labelTicker;

    private List<LiquidityZone> liquidityZoneList = null;

    private String ticker;
    private MainPresenter mainPresenter;
    private PredictionPresenter predictionPresenter;
    private DetailsPresenter detailsPresenter;
    private GapAnalysisPresenter gapAnalysisPresenter;

    @Override
    public void start(Stage primaryStage) throws Exception {
        resourceLoader.setContent("layout_main");
        resourceLoader.setStyle("style");
        resourceLoader.inflate(primaryStage, "Stock Analyzer");

        tfTicker = (TextField) resourceLoader.findView("tf_ticker");
        tfTopPercentFilter = (TextField) resourceLoader.findView("tf_percent_filter");
        tfPrice = (TextField) resourceLoader.findView("tf_price");
        tfPredictionPrice = (TextField) resourceLoader.findView("tf_prediction_price");
        taGapAnalysis = (TextArea) resourceLoader.findView("ta_gap_analysis");
        tfGapPercent = (TextField) resourceLoader.findView("tf_gap_percent");

        labelTicker = (Label) resourceLoader.findView("label_ticker");
        labelError = (Label) resourceLoader.findView("label_error");
        labelLoading = (Label) resourceLoader.findView("label_loading");
        piLoading = (ProgressIndicator) resourceLoader.findView("pi_loading");

        tableViewZones = (TableView) resourceLoader.findView("table_supply_zones");
        tablePrediction = (TableView) resourceLoader.findView("table_prediction");

        btnCreateReport = (Button) resourceLoader.findView("btn_create_report");
        btnCreateReport.setOnAction((ActionEvent event) -> {
            createReport();
        });

        btnFilter = (Button) resourceLoader.findView("btn_filter_precent");
        btnFilter.setOnAction((ActionEvent event) -> {
            filter();
        });

        btnCreatePrediction = (Button) resourceLoader.findView("btn_predict");
        btnCreatePrediction.setOnAction((ActionEvent event) -> {
            createPrediction();
        });

        btnGapAnalysis = (Button) resourceLoader.findView("btn_analyse_gap");
        btnGapAnalysis.setOnAction((ActionEvent event) -> {
            analyzeGap();
        });

        btnCreateHeatMap = (Button) resourceLoader.findView("btn_heat_map");

        cbNews = (ChoiceBox) resourceLoader.findView("cb_news");

        rbAlphaVantage = (RadioButton) resourceLoader.findView("rb_alpha_vantage");
        rbYahoo = (RadioButton) resourceLoader.findView("rb_yahoo");

        vpaRuleOne = (RadioButton) resourceLoader.findView("rb_one_point");
        vpaRuleFour = (RadioButton) resourceLoader.findView("rb_four_point");
        vpaRuleFive = (RadioButton) resourceLoader.findView("rb_five_point");

        ObservableList<News> news = FXCollections.observableArrayList();
        news.add(News.None);
        news.add(News.Positive);
        news.add(News.Negative);

        cbNews.setItems(news);
        cbNews.setValue(news.get(0));

        cbTrend = (ChoiceBox) resourceLoader.findView("cb_trend");

        ObservableList<Trend> trends = FXCollections.observableArrayList();
        trends.add(Trend.Consolidating);
        trends.add(Trend.Bullish);
        trends.add(Trend.Bearish);

        cbTrend.setItems(trends);
        cbTrend.setValue(trends.get(0));

        rbGap = (RadioButton) resourceLoader.findView("rb_gap");

        prepareSupplyTable();
        prepareTablePrediction();

        mainPresenter = new MainPresenterImpl(ThreadExecutor.getInstance(), new JavaFxThread(), this);
        predictionPresenter = new PredictionPresenterImpl(this);
        gapAnalysisPresenter = new GapAnalysisPresenterImpl(ThreadExecutor.getInstance(), new JavaFxThread(), this);
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void populateSupplyZones(List<LiquidityZone> data) {
        tableViewZones.getItems().clear();
        tableViewZones.getItems().addAll(data);
    }

    private void populatePrediction(List<Node> data) {
        tablePrediction.getItems().clear();
        tablePrediction.getItems().addAll(data);
    }

    private void prepareSupplyTable() {
        TableColumn headerPrice = new TableColumn("Price");
        headerPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        TableColumn headerVol = new TableColumn("Volume");
        headerVol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        headerVol.setCellFactory(tc -> new TableCell<LiquidityZone, Number>() {
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

        TableColumn headerRelVol = new TableColumn("RelVolume");
        headerRelVol.setCellValueFactory(new PropertyValueFactory<>("relativeVolume"));
        headerRelVol.setCellFactory(tc -> new TableCell<LiquidityZone, Number>() {
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

        TableColumn headerVolPer = new TableColumn("VolPercent");
        headerVolPer.setCellValueFactory(new PropertyValueFactory<>("volumePercentage"));
        headerVolPer.setCellFactory(tc -> new TableCell<LiquidityZone, Number>() {
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

        TableColumn headerPercntile = new TableColumn("Percentile");
        headerPercntile.setCellValueFactory(new PropertyValueFactory<>("percentile"));
        headerPercntile.setCellFactory(tc -> new TableCell<LiquidityZone, Number>() {
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

        tableViewZones.getColumns().addAll(headerPrice, headerVol, headerRelVol, headerVolPer, headerPercntile);

        tableViewZones.setRowFactory(tv -> new TableRow<LiquidityZone>() {
            @Override
            protected void updateItem(LiquidityZone item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    if (!item.origin) {

                        String color = ColorHelper.getColorForPercentile(item.percentile);

                        if (color != null) {

                            setStyle("-fx-background-color: " + color + ";");
                        } else {
                            setStyle("");
                        }

                    } else {
                        setStyle("");
                    }
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void prepareTablePrediction() {
        TableColumn headerPrice = new TableColumn("Price");
        headerPrice.setSortable(false);
        headerPrice.setCellValueFactory(new PropertyValueFactory<>("level"));

        TableColumn headerChange = new TableColumn("Change");
        headerChange.setSortable(false);
        headerChange.setCellValueFactory(new PropertyValueFactory<>("change"));
        headerChange.setCellFactory(tc -> new TableCell<Node, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        TableColumn headerPrediction = new TableColumn("Prediction");
        headerPrediction.setSortable(false);
        headerPrediction.setCellValueFactory(new PropertyValueFactory<>("prediction"));
        headerPrediction.setCellFactory(tc -> new TableCell<Node, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(String.format("%.2f", value.doubleValue()));
                }
            }
        });

        tablePrediction.getColumns().addAll(headerPrice, headerChange, headerPrediction);

        tablePrediction.setRowFactory(tv -> new TableRow<Node>() {
            @Override
            protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    if (item.origin) {
                        setStyle("-fx-background-color: #8BC7FF;");
                    } else {

                        String color = ColorHelper.getColorForStrength(item.prediction, 75);

                        if (color == null) {
                            setStyle("");
                        } else {
                            setStyle("-fx-background-color: " + color + ";");
                        }

                    }
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void createReport() {

        ticker = tfTicker.getText();

        if (!ticker.isEmpty()) {
            int data = 0;
            if (rbAlphaVantage.isSelected()) {
                data = 1;
            }

            int vpaRule = 0;
            if(vpaRuleFour.isSelected()){
                vpaRule = 1;
            }

            if(vpaRuleFive.isSelected()){
                vpaRule = 2;
            }

            mainPresenter.getZones(ticker, data, vpaRule);
        }
    }


    private void createPrediction() {
        String price = tfPredictionPrice.getText();

        if (StringUtils.isInteger(price)) {
            if (liquidityZoneList != null) {
                int cents = Integer.parseInt(price);
                int news = ((News) cbNews.getSelectionModel().getSelectedItem()).getValue();
                int trend = ((Trend) cbTrend.getSelectionModel().getSelectedItem()).getValue();

                if (rbGap.isSelected()) {
                    predictionPresenter.startPrediction(liquidityZoneList, cents, news, trend, gapAnalysisPresenter.getGapResult());
                } else {
                    predictionPresenter.startPrediction(liquidityZoneList, cents, news, trend, null);
                }
            }
        }
    }

    private void filter() {
        String percent = tfTopPercentFilter.getText();
        if (StringUtils.idDouble(percent)) {
            mainPresenter.filterZones(Double.parseDouble(percent));
        }
    }

    private void analyzeGap() {
        if (ticker != null) {
            gapAnalysisPresenter.analyseGaps(ticker);
        }
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
    public void onZonesCreated(List<LiquidityZone> zones) {
        liquidityZoneList = zones;
        labelTicker.setText("Ticker : " + ticker.toUpperCase());
        populateSupplyZones(zones);
        populatePrediction(new ArrayList<>());
    }

    @Override
    public void onZonesFiltered(List<LiquidityZone> zones) {
        liquidityZoneList = zones;
        populateSupplyZones(zones);
        populatePrediction(new ArrayList<>());
    }

    @Override
    public void onPredictionCompleted(List<Node> nodes) {
        populatePrediction(nodes);
    }

    @Override
    public void onGapAnalysisFinished(String details) {
        taGapAnalysis.setText(details);
    }

    private void createHeatMap() {
    }

}
