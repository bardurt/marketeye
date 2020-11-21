package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDataInteractor extends BaseInteractor implements DataFetchInteractor {

    private Callback callback;
    private final String ticker;
    private final TimeFrame timeFrame;
    private final int monthsToFetch;
    private final String apiKey;

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
            lines = downloadIntraDay(ticker, interval, monthsToFetch, apiKey);
        } else {
            lines = downloadDaily(ticker, apiKey);
        }

        if (lines.isEmpty()) {
            mainThread.post(() -> callback.onDataFetchError("Unable to download data for " + ticker));
        }

        mainThread.post(() -> callback.onDataFetched(lines, ticker, timeFrame));
    }

    /**
     * Method to download Intra day Historical data from AlphaVantage (AV)
     *
     * @param symbol        : the Symbol (ticker)
     * @param interval      : time interval for historical data (1 min, 3 min)
     * @param monthsToFetch : how many months of data should be downloaded
     * @param apiKey        : Api key for AV
     */
    private List<String> downloadIntraDay(String symbol, String interval, int monthsToFetch, String apiKey) {

        // container for the read historical data
        List<String> lines = new ArrayList<>();

        // Flag to check if downloading data is finished or not
        boolean finished = false;

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        URLConnection urlConnection;

        // start by downloading data for the most recent year.
        int year = 1; // year 1 month 1 is the most recent month

        // counter for checking how many months have been downloaded
        int currentCount = 0;

        while (year < 3) {

            // start with download the latest month of year
            int month = 1;

            // loop through all the month for the year
            while (month < 13) {

                String slice = "year" + year + "month" + month;

                String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&symbol=" + symbol.toUpperCase() + "&interval=" + interval + "&slice=" + slice + "&apikey=" + apiKey;

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
                        if (line.equalsIgnoreCase(",,,,,")) {
                            finished = true;
                            break;
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
                } catch (Exception e) {
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

                if (finished) {
                    break;
                }

                // finish this month
                month++;

                // add this month to total count
                currentCount++;

                // check if we have fetched all the months requested
                if (currentCount >= monthsToFetch) {
                    finished = true;
                    break;
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

    private List<String> downloadDaily(String symbol, String apiKey) {

        List<String> lines = new ArrayList<>();

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + symbol + "&outputsize=full&apikey=" + apiKey + "&datatype=csv";

        System.out.println(url);
        try {
            URL content = new URL(url);

            // establish connection to file in URL
            URLConnection urlConn = content.openConnection();

            // ...
            InputStreamReader inputCSV = new InputStreamReader(
                    urlConn.getInputStream());
            // ...
            BufferedReader br = new BufferedReader(inputCSV);

            String line;

            while ((line = br.readLine()) != null) {

                if (line.equalsIgnoreCase(",,,,,")) {
                    break;
                }
                String data = splitStringAdjusted(line);
                if (data != null) {
                    lines.add(data);
                }
            }

            br.close();
            inputCSV.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    private String splitStringAdjusted(String line) {

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

        return parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + parts[6];
    }
}
