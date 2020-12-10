package com.zygne.stockanalyzer;

import com.zygne.stockanalyzer.domain.DataBroker;
import com.zygne.stockanalyzer.domain.exceptions.ApiCallExceededException;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDataBroker implements DataBroker {

    private static final String ERROR_INPUT = "Thank you for using Alpha Vantage!";

    private static final String FILE_TERMINATOR = ",,,,,";
    private static final String TIME_SERIES_DAILY = "TIME_SERIES_DAILY";
    private static final String TIME_SERIES_WEEKLY = "TIME_SERIES_WEEKLY";
    private static final String TIME_SERIES_MONTHLY = "TIME_SERIES_MONTHLY";
    private static final int MAX_FAILURES = 3;

    private static final long TIME_TO_SLEEP = 15000;
    private String ticker;
    private TimeInterval timeInterval;
    private int monthsToFetch;
    private String apiKey;
    private int failures = 0;

    @Override
    public void downloadData(String symbol, String length, String interval) {

    }

    @Override
    public void setCallback(Callback callback) {

    }

    @Override
    public void removeCallback() {

    }

    @Override
    public void connect() { }

    @Override
    public void disconnect() { }

    @Override
    public void setConnectionListener(ConnectionListener connectionListener) { }

    @Override
    public void removeConnectionListener() { }

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
                    if (failures > MAX_FAILURES) {
                        finished = true;
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
