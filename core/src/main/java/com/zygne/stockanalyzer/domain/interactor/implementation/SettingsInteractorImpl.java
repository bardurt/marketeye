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
    private static final String TAG_PROVIDER = "DATA_PROVIDER";

    private static final String INDENT = "   ";
    private final Callback callback;

    public SettingsInteractorImpl(Executor executor, MainThread mainThread, Callback callback) {
        super(executor, mainThread);
        this.callback = callback;
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


        try {
            fileReader = new FileReader(file);
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

            String provider = TagHelper.getValueFromTagName(TAG_PROVIDER, content.toString());

            if (provider.isEmpty()) {
                mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
            } else {

                if (provider.equalsIgnoreCase(TAG_ALPHA_VANTAGE)) {
                    String avData = TagHelper.getValueFromTagName(TAG_ALPHA_VANTAGE, content.toString());
                    String apiKey = TagHelper.getValueFromTagName(TAG_API, avData.toString());
                    String cache = TagHelper.getValueFromTagName(TAG_CACHE, avData.toString());

                    if (apiKey.isEmpty()) {
                        mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
                    } else {
                        Settings settings = new Settings();
                        settings.setApiKey(apiKey);
                        settings.setCache(cache);
                        settings.setDataProvider(DataProvider.ALPHA_VANTAGE);
                        mainThread.post(() -> callback.onSettingsLoaded(settings));
                    }
                } else {

                    String ibData = TagHelper.getValueFromTagName(TAG_INTERACTIVE_BROKERS, content.toString());
                    String cache = TagHelper.getValueFromTagName(TAG_CACHE, ibData.toString());

                    if (cache.isEmpty()) {
                        mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
                    } else {
                        Settings settings = new Settings();
                        settings.setApiKey("");
                        settings.setCache(cache);
                        settings.setDataProvider(DataProvider.INTERACTIVE_BROKERS);
                        mainThread.post(() -> callback.onSettingsLoaded(settings));
                    }
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
        stringBuilder.append(TagHelper.createTag(TAG_PROVIDER, TAG_ALPHA_VANTAGE));
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
