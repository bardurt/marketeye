package com.zygne.stockanalyzer.domain.interactor.implementation;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import com.zygne.stockanalyzer.domain.utils.TagHelper;

import java.io.*;

public class SettingsInteractorImpl extends BaseInteractor implements SettingsInteractor {

    private static final String TAG_API = "API_KEY";
    private static final String TAG_CACHE = "CACHE";
    private static final String SETTINGS_FILE = "config.xml";
    private static final String TAG_ALPHA_VANTAGE = "AV";
    private static final String TAG_INTERACTIVE_BROKERS = "IB";
    private static final String TAG_YAHOO_FINANCE = "YF";

    private static final String INDENT = "   ";
    private final Callback callback;
    private DataProvider defaultProvider = null;

    public SettingsInteractorImpl(Executor executor, MainThread mainThread, Callback callback) {
        super(executor, mainThread);
        this.callback = callback;
        this.defaultProvider = null;
    }

    public SettingsInteractorImpl(Executor executor, MainThread mainThread, Callback callback, DataProvider dataProvider) {
        super(executor, mainThread);
        this.callback = callback;
        this.defaultProvider = dataProvider;
    }

    @Override
    public void run() {
        String path = SETTINGS_FILE;

        File file = new File(path);

        if (file.exists()) {
            readSettings(file);
        } else {
            createSettings(path);
            mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
        }
    }

    private void readSettings(File file) {
        FileReader fileReader = null;
        String settingsPath = "";

        try {
            fileReader = new FileReader(file);
            settingsPath = file.getAbsolutePath();
        } catch (Exception ignored) {
        }

        StringBuilder content = new StringBuilder();

        if (fileReader != null) {
            try {
                BufferedReader br = new BufferedReader(fileReader);
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (content.length() == 0) {
            mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
        } else {

            String provider;

            if (defaultProvider == null) {
                provider = TAG_YAHOO_FINANCE;
            } else {
                if(defaultProvider == DataProvider.ALPHA_VANTAGE){
                    provider = TAG_ALPHA_VANTAGE;
                } else if (defaultProvider == DataProvider.INTERACTIVE_BROKERS) {
                    provider = TAG_INTERACTIVE_BROKERS;
                } else {
                    provider = TAG_YAHOO_FINANCE;
                }
            }

            if (provider.isEmpty()) {
                mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
            } else {

                if (provider.equalsIgnoreCase(TAG_ALPHA_VANTAGE)) {
                    String avData = TagHelper.getValueFromTagName(TAG_ALPHA_VANTAGE, content.toString());
                    String apiKey = TagHelper.getValueFromTagName(TAG_API, avData);
                    String cache = TagHelper.getValueFromTagName(TAG_CACHE, avData);

                    if (apiKey.isEmpty()) {
                        mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
                    } else {
                        Settings settings = new Settings();
                        settings.setPath(settingsPath);
                        settings.setApiKey(apiKey);
                        settings.setCache(cache);
                        settings.setDataProvider(DataProvider.ALPHA_VANTAGE);
                        mainThread.post(() -> callback.onSettingsLoaded(settings));
                    }
                } else if (provider.equalsIgnoreCase(TAG_INTERACTIVE_BROKERS))  {

                    String ibData = TagHelper.getValueFromTagName(TAG_INTERACTIVE_BROKERS, content.toString());
                    String cache = TagHelper.getValueFromTagName(TAG_CACHE, ibData);

                    if (cache.isEmpty()) {
                        mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
                    } else {
                        Settings settings = new Settings();
                        settings.setPath(settingsPath);
                        settings.setApiKey("");
                        settings.setCache(cache);
                        settings.setDataProvider(DataProvider.INTERACTIVE_BROKERS);
                        mainThread.post(() -> callback.onSettingsLoaded(settings));
                    }
                }else {
                    Settings settings = new Settings();
                    settings.setPath(settingsPath);
                    settings.setApiKey("");
                    settings.setCache("");
                    settings.setDataProvider(DataProvider.YAHOO_FINANCE);
                    mainThread.post(() -> callback.onSettingsLoaded(settings));
                }
            }
        }

    }

    private void createSettings(String filename) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(getSettingsContent());
            myWriter.close();
        } catch (IOException ignored) {
        }

    }

    private String getSettingsContent() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!-- Configuration file -->");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("<!-- Alpha Vantage Details-->");
        stringBuilder.append("\n");
        stringBuilder.append(TagHelper.start(TAG_ALPHA_VANTAGE));
        stringBuilder.append("\n");
        stringBuilder.append(INDENT);
        stringBuilder.append("<!-- API Key for https://www.alphavantage.co/ -->");
        stringBuilder.append("\n");
        stringBuilder.append(INDENT);
        stringBuilder.append(TagHelper.createTag(TAG_API, "MY_ALPHA_VANTAGE_API_KEY"));
        stringBuilder.append("\n");
        stringBuilder.append(INDENT);
        stringBuilder.append(TagHelper.createTag(TAG_CACHE, "cache_data_av"));
        stringBuilder.append("\n");
        stringBuilder.append(TagHelper.end(TAG_ALPHA_VANTAGE));
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("<!-- Interactive Brokers Details-->");
        stringBuilder.append("\n");
        stringBuilder.append(TagHelper.start(TAG_INTERACTIVE_BROKERS));
        stringBuilder.append("\n");
        stringBuilder.append(INDENT);
        stringBuilder.append(TagHelper.createTag(TAG_CACHE, "cache_data_ib"));
        stringBuilder.append("\n");
        stringBuilder.append(TagHelper.end(TAG_INTERACTIVE_BROKERS));

        return stringBuilder.toString();
    }
}
