package com.zygne.stockanalyzer;

import com.ib.client.*;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.text.SimpleDateFormat;
import java.util.*;

public class IbDataBroker implements DataBroker, EWrapper {

    private static final class Identifiers {
        private static final int HISTORICAL_BAR_DATA = 4001;
    }

    private static final class Host {
        private static final String IP = "127.0.0.1";
        private static final int PORT = 7497;
    }

    private static final class Equity {
        private static final String TYPE = "STK";
        private static final String CURRENCY = "USD";
        private static final String EXCHANGE = "ISLAND";
    }

    private static final class Format {
        private static final int TRADING_HOURS = 0; // 0 for extending, 1 for regular
        private static final int TIME_FORMAT = 1; // 1 for yyyyMMDD HH:mm:ss, 0 for UNIX
        private static final String PRICES = "TRADES";
        private static final boolean REAL_TIME = false;
    }

    private static final class ErrorCodes {
        private static final int BAD_MESSAGE = 507;
        private static final int CONNECTION_ERROR = 502;
    }

    private static final int SCALAR = 100;
    private EReaderSignal readerSignal;
    private EClientSocket clientSocket;
    private EReader reader;

    private final List<BarData> barDataList = new ArrayList<>();

    private ConnectionListener connectionListener;
    private Callback callback;

    @Override
    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    @Override
    public void removeConnectionListener() {
        this.connectionListener = null;
    }

    @Override
    public void connect() {
        readerSignal = new EJavaSignal();
        clientSocket = new EClientSocket(this, readerSignal);
        clientSocket.eConnect(Host.IP, Host.PORT, 2);

        reader = new EReader(clientSocket, readerSignal);

        reader.start();

        new Thread(() -> {
            while (clientSocket.isConnected()) {
                readerSignal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    @Override
    public void disconnect() {
        clientSocket.eDisconnect();

        if (connectionListener != null) {
            connectionListener.onApiDisconnected();
        }
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void removeCallback() {
        this.callback = null;
    }

    @Override
    public void downloadHistoricalBarData(String symbol, DataSize dataSize, TimeInterval timeInterval) {
        barDataList.clear();
        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType(Equity.TYPE);
        contract.currency(Equity.CURRENCY);
        contract.exchange(Equity.EXCHANGE);

        String interval = "1 min";

        switch (timeInterval) {
            case One_Minute:
                interval = "1 min";
                break;
            case Three_Minutes:
                interval = "3 mins";
                break;
            case Five_Minutes:
                interval = "5 mins";
                break;
            case Fifteen_Minutes:
                interval = "15 mins";
                break;
            case Thirty_Minutes:
                interval = "30 mins";
                break;
            case Hour:
                interval = "1 hour";
                break;
            case Day:
                interval = "1 day";
                break;
            case Week:
                interval = "1 week";
                break;
            case Month:
                interval = "1 month";
                break;
        }


        String length = dataSize.getSize() + "";

        if(dataSize.getUnit() == DataSize.Unit.Year){
            length += " Y";
        } else {
            length += " D";
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String formatted = form.format(cal.getTime());

        System.out.println(formatted);

        clientSocket.reqHistoricalData(Identifiers.HISTORICAL_BAR_DATA, contract, formatted, length, interval, Format.PRICES, Format.TRADING_HOURS, Format.TIME_FORMAT, Format.REAL_TIME, null);

    }

    @Override
    public void nextValidId(int orderId) {
        connectionListener.onApiConnected();
    }

    @Override
    public void historicalData(int reqId, Bar bar) {
        BarData b = new BarData(bar.time(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume() * SCALAR);
        b.setDataFarm(BarData.DataFarm.INTERACTIVE_BROKERS);
        barDataList.add(b);
    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        for (HistogramEntry e : items) {
            BarData b = new BarData("", e.price, e.price, e.price, e.price, e.size);
            barDataList.add(b);
        }
    }

    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        if (callback != null) {
            callback.onDataFinished(barDataList);
        }
    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {
        System.out.println("Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg + "\n");

        if (id == Identifiers.HISTORICAL_BAR_DATA) {
            if (callback != null) {
                callback.onDataFinished(barDataList);
            }
            return;
        }

        if (errorCode == ErrorCodes.BAD_MESSAGE) {
            connectionListener.onApiDisconnected();
            return;
        }

        if (errorCode == ErrorCodes.CONNECTION_ERROR) {
            connectionListener.onApiDisconnected();
            return;
        }

    }

    @Override
    public void error(String str) {
        System.out.println("Error: " + str);
    }

    @Override
    public void connectionClosed() {
        if (connectionListener != null) {
            connectionListener.onApiDisconnected();
        }
    }

    @Override
    public void connectAck() {
        if (clientSocket.isAsyncEConnect()) {
            clientSocket.startAPI();
        }
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickOptionComputation(int tickerId, int field,
                                      double impliedVol, double delta, double optPrice,
                                      double pvDividend, double gamma, double vega, double theta,
                                      double undPrice) {
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints,
                        String formattedBasisPoints, double impliedFuture, int holdDays,
                        String futureLastTradeDate, double dividendImpact,
                        double dividendsToLastTradeDate) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void orderStatus(int orderId, String status, double filled,
                            double remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order,
                          OrderState orderState) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void openOrderEnd() {
        throw new RuntimeException("Stub");
    }

    @Override
    public void updateAccountValue(String key, String value, String currency,
                                   String accountName) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void updatePortfolio(Contract contract, double position,
                                double marketPrice, double marketValue, double averageCost,
                                double unrealizedPNL, double realizedPNL, String accountName) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void updateAccountTime(String timeStamp) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void accountDownloadEnd(String accountName) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void execDetailsEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation,
                               int side, double price, int size) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position,
                                 String marketMaker, int operation, int side, double price, int size, boolean isSmartDepth) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message,
                                   String origExchange) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void managedAccounts(String accountsList) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void receiveFA(int faDataType, String xml) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void scannerParameters(String xml) {
        throw new RuntimeException("Stub");
    }

    //! [scannerdata]
    @Override
    public void scannerData(int reqId, int rank,
                            ContractDetails contractDetails, String distance, String benchmark,
                            String projection, String legsStr) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void scannerDataEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high,
                            double low, double close, long volume, double wap, int count) {
        throw new RuntimeException("Stub");
    }


    @Override
    public void currentTime(long time) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void fundamentalData(int reqId, String data) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void position(String account, Contract contract, double pos,
                         double avgCost) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void positionEnd() {
        throw new RuntimeException("Stub");
    }

    @Override
    public void accountSummary(int reqId, String account, String tag,
                               String value, String currency) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void accountSummaryEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void verifyMessageAPI(String apiData) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
        throw new RuntimeException("Stub");
    }

    //! [displaygrouplist]
    @Override
    public void displayGroupList(int reqId, String groups) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void error(Exception e) {
        connectionListener.onApiDisconnected();
    }


    @Override
    public void positionMulti(int reqId, String account, String modelCode,
                              Contract contract, double pos, double avgCost) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void positionMultiEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode,
                                   String key, String value, String currency) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange,
                                                    int underlyingConId, String tradingClass, String multiplier,
                                                    Set<String> expirations, Set<Double> strikes) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Map.Entry<String, Character>> theMap) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        throw new RuntimeException("Stub");
    }


    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        throw new RuntimeException("Stub");
    }


    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void pnlSingle(int reqId, int pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, int size, TickAttribLast tickAttribLast,
                                  String exchange, String specialConditions) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize,
                                 TickAttribBidAsk tickAttribBidAsk) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        throw new RuntimeException("Stub");
    }

    @Override
    public void completedOrdersEnd() {
        throw new RuntimeException("Stub");
    }

}
