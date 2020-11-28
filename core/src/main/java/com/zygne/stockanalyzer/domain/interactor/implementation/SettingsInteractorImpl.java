package com.zygne.stockanalyzer.domain.interactor.implementation;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.utils.TagHelper;

import java.io.*;

public class SettingsInteractorImpl extends BaseInteractor implements SettingsInteractor {

    private static final String TAG_API = "API_KEY";
    private static final String TAG_CACHE = "CACHE";
    private static final String SETTINGS_FILE = "config.xml";
    private final Callback callback;

    public SettingsInteractorImpl(Executor executor, MainThread mainThread, Callback callback) {
        super(executor, mainThread);
        this.callback = callback;
    }

    @Override
    public void run() {
        String path = SETTINGS_FILE;

        File file = new File(path);

        if(file.exists()){
            readSettings(file);
        } else {
            createSettings(path);
            mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
        }
    }

    private void readSettings(File file){
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

        if(content.length() == 0){
            mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
        } else {

            String apiKey = TagHelper.getValueFromTagName(TAG_API, content.toString());
            String cache = TagHelper.getValueFromTagName(TAG_CACHE, content.toString());

            if(apiKey.isEmpty()){
                mainThread.post(() -> callback.onSettingsError(SETTINGS_FILE));
            } else {
                Settings settings = new Settings();
                settings.setApiKey(apiKey);
                settings.setCache(cache);
                mainThread.post(() -> callback.onSettingsLoaded(settings));
            }
        }

    }

    private void createSettings(String filename){
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(getSettingsContent());
            myWriter.close();
        } catch (IOException ignored) { }

    }

    private String getSettingsContent(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!-- Configuration file -->");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("<!-- API Key for https://www.alphavantage.co/ -->");
        stringBuilder.append("\n");
        stringBuilder.append(TagHelper.createTag(TAG_API, "MY_ALPHA_VANTAGE_API_KEY"));
        stringBuilder.append("\n");
        stringBuilder.append(TagHelper.createTag(TAG_CACHE, "cache_data"));
        return stringBuilder.toString();
    }
}
