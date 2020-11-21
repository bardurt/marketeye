package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.JavaFxThread;
import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.executor.ThreadExecutor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.model.Settings;

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

            String apiKey = getValueFromTagName(TAG_API, content.toString());
            String cache = getValueFromTagName(TAG_CACHE, content.toString());

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

        stringBuilder.append("<!-- Configuratiin file -->");
        stringBuilder.append("\n");
        stringBuilder.append(createTag(TAG_API, "MY_ALPHA_VANTAGE_API_KEY"));
        stringBuilder.append("\n");
        stringBuilder.append(createTag(TAG_CACHE, "cache_data"));
        return stringBuilder.toString();
    }

    protected String getValueFromTagName(String name, String raw) {

        if (raw.isEmpty()) {
            return "";
        }

        String startTag = "<" + name + ">";
        String endTarget = "</" + name + ">";

        String[] firstParts = raw.split(endTarget, -1);

        // if size is not 2, then one of the following error has happened
        // 1 : end tag does not exists in the raw data
        // 2 : end tag appears more than 1 time.
        // both of these cases makes the XML invalid, so return empty
        if (firstParts.length != 2) {
            return "";
        }

        String[]  secondParts = firstParts[0].split(startTag, -1);

        // if size is not 2, then one of the following error has happened
        // 1 : start tag does not exists in the raw data
        // 2 : start tag appears more than 1 time.
        // both of these cases makes the XML invalid, so return empty
        if (secondParts.length != 2) {
            return "";
        }

        return secondParts[1];
    }

    private String createTag(String name, String value){
        String startTag = "<" + name + ">";
        String endTag = "</" + name + ">";

        return startTag+value+endTag;
    }


    public static void main(String[] args) {

        new SettingsInteractorImpl(ThreadExecutor.getInstance(),
                new JavaFxThread(),
                new Callback() {
            @Override
            public void onSettingsLoaded(Settings settings) {

            }

            @Override
            public void onSettingsError(String filename) {

            }
        }).execute();

    }
}
