package com.zygne.stockanalyzer.domain.model;

import com.zygne.stockanalyzer.domain.model.enums.DataProvider;

public class Settings {

    private String path;
    private String apiKey;
    private String cache;
    private DataProvider dataProvider;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String toString() {
        String value = "Path : " + path + "\n"
                + "Data Provider : " + dataProvider.toString() + "\n"
                + "Cache : " + cache;

        if (dataProvider == DataProvider.ALPHA_VANTAGE || dataProvider == DataProvider.CRYPTO_COMPARE) {
            value += "\n" + "API KEY ; " + apiKey;
        }

        return value;
    }

}
