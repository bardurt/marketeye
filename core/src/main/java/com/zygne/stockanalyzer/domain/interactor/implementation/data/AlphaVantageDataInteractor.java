package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.exceptions.ApiCallExceededException;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockanalyzer.domain.model.enums.TimeFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDataInteractor extends BaseInteractor implements DataFetchInteractor {

    private static final String ERROR_INPUT = "Thank you for using Alpha Vantage!";

    private static final String FILE_TERMINATOR = ",,,,,";
    private static final String TIME_SERIES_DAILY = "TIME_SERIES_DAILY";
    private static final String TIME_SERIES_WEEKLY = "TIME_SERIES_WEEKLY";
    private static final String TIME_SERIES_MONTHLY = "TIME_SERIES_MONTHLY";
    private static final int MAX_FAILURES = 3;

    private static final long TIME_TO_SLEEP = 15000;
    private final Callback callback;
    private final String ticker;
    private final TimeFrame timeFrame;
    private final int monthsToFetch;
    private final String apiKey;
    private int failures = 0;

    public AlphaVantageDataInteractor(Executor executor, MainThread mainThread, Callback callback, String ticker, TimeFrame timeFrame, int monthsToFetch, String apiKey) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
        this.timeFrame = timeFrame;
        this.monthsToFetch = monthsToFetch;
        this.apiKey = apiKey;
    }

    @Override
    public void run() {

        String interval = null;

        if (timeFrame == TimeFrame.One_Minute) {
            interval = "1min";
        } else if (timeFrame == TimeFrame.Three_Minutes) {
            interval = "3min";
        } else if (timeFrame == TimeFrame.Five_Minutes) {
            interval = "5min";
        } else if (timeFrame == TimeFrame.Fifteen_Minutes) {
            interval = "15min";
        } else if (timeFrame == TimeFrame.Thirty_Minutes) {
            interval = "30min";
        } else if (timeFrame == TimeFrame.Hour) {
            interval = "60min";
        } else if (timeFrame == TimeFrame.Day) {
            interval = null;
        }

        List lines;

        if (interval != null) {
            mainThread.post(() -> callback.onStatusUpdate("Will download intra day data for " + ticker + ", " + monthsToFetch + " months"));
            lines = fetchIntraDay(ticker, interval, monthsToFetch, apiKey);
        } else {

            String timeSeries = TIME_SERIES_DAILY;

            if (timeFrame == TimeFrame.Week) {
                timeSeries = TIME_SERIES_WEEKLY;
            } else if(timeFrame == TimeFrame.Month){
                timeSeries = TIME_SERIES_MONTHLY;
            }

            lines = downLoadTimeSeries(ticker, timeSeries, apiKey);
        }

        if (lines.isEmpty()) {
            mainThread.post(() -> callback.onDataFetchError("Unable to download data for " + ticker));
        } else {
            mainThread.post(() -> callback.onDataFetched(lines));
        }
    }

    private List<String> fetchIntraDay(String symbol, String interval, int monthsToFetch, String apiKey) {
        List<String> lines = new ArrayList<>();

        // Flag to check if downloading data is finished or not
        boolean finished = false;

        // start by downloading data for the most recent year.
        int year = 1; // year 1 month 1 is the most recent month

        // counter for checking how many months have been downloaded
        int currentCount = 0;

        while (year < 3) {

            // start with download the latest month of year
            int month = 1;

            // loop through all the month for the year
            while (month < 13) {

                String time = "year" + year + "month" + month;
                List<String> data = null;
                try {
                    data = downloadIntraDay(symbol, interval, apiKey, time);
                    if (data.isEmpty()) {
                        finished = true;
                        break;
                    }
                    lines.addAll(data);
                    failures = 0;
                } catch (ApiCallExceededException e) {
                    month--;
                    currentCount--;
                    failures++;
                    mainThread.post(() -> callback.onStatusUpdate("API CALL ERROR"));
                    if (failures > MAX_FAILURES) {
                        finished = true;
                        mainThread.post(() -> callback.onStatusUpdate("Download terminated"));
                    }
                }

                // finish this month
                month++;

                // add this month to total count
                currentCount++;

                // check if we have fetched all the months requested
                if (currentCount >= monthsToFetch) {
                    finished = true;
                }

                // we are finished, so no need to continue
                if (finished) {
                    break;
                }

                try {
                    mainThread.post(() -> callback.onStatusUpdate("Waiting " + TIME_TO_SLEEP + " ms for next."));
                    Thread.sleep(TIME_TO_SLEEP);
                } catch (InterruptedException ignored) {
                }
            }

            // we are finished, so no need to continue
            if (finished) {
                break;
            }

            // finish this year
            year++;
        }

        return lines;
    }

    /**
     * Method to download Intra day Historical data from AlphaVantage (AV)
     *
     * @param symbol   : the Symbol (ticker)
     * @param interval : time interval for historical data (1 min, 3 min)
     * @param apiKey   : Api key for AV
     */
    private List<String> downloadIntraDay(String symbol, String interval, String apiKey, String time) throws ApiCallExceededException {

        // container for the read historical data
        List<String> lines = new ArrayList<>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        URLConnection urlConnection;


        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&symbol=" + symbol.toUpperCase() + "&interval=" + interval + "&slice=" + time + "&apikey=" + apiKey;

        mainThread.post(() -> callback.onStatusUpdate("Downloading : " + url));

        try {
            // prepare the url for Alpha Vantage
            URL content = new URL(url);

            // open connection to URL
            urlConnection = content.openConnection();

            // fetch stream for reading data
            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            // reader to read data from stream
            bufferedReader = new BufferedReader(inputStreamReader);

            // the line which is currently being read
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equalsIgnoreCase(FILE_TERMINATOR)) {
                    break;
                }

                if(!validateInput(line)){
                    try {
                        bufferedReader.close();
                        bufferedReader = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        inputStreamReader.close();
                        inputStreamReader = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    throw new ApiCallExceededException();
                }

                // clean up the read data
                String data = splitString(line);

                // if the read data is null, then it is most likely a header
                if (data != null) {
                    lines.add(data);
                }
            }

            // clean up connection
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    bufferedReader = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                    inputStreamReader = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        mainThread.post(() -> callback.onStatusUpdate("Download finished"));

        return lines;
    }

    private List<String> downLoadTimeSeries(String symbol, String series, String apiKey) {

        List<String> lines = new ArrayList<>();

        String url = "https://www.alphavantage.co/query?function=" + series + "&symbol=" + symbol + "&outputsize=full&apikey=" + apiKey + "&datatype=csv";

        mainThread.post(() -> callback.onStatusUpdate("Downloading : " + url));

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        URLConnection urlConnection = null;
        try {
            URL content = new URL(url);

            // establish connection to file in URL
            urlConnection = content.openConnection();

            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String data = splitString(line);
                if (data != null) {
                    lines.add(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mainThread.post(() -> callback.onStatusUpdate("Download finished"));

        return lines;
    }

    private String splitString(String line) {

        String[] parts = line.split(",", -1);

        if (parts[0].equalsIgnoreCase("time")) {
            return null;
        }

        if (parts[0].equalsIgnoreCase("timestamp")) {
            return null;
        }

        if (parts.length < 5) {
            return null;
        }

        return line;
    }

    private boolean validateInput(String input){

        if(input.contains(ERROR_INPUT)){
            System.out.println(input);
            return false;
        }

        return true;
    }

}
