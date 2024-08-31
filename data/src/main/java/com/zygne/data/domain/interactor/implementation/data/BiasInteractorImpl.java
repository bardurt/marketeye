package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;
import com.zygne.data.FileWriter;
import com.zygne.data.domain.interactor.implementation.data.base.BiasInteractor;
import com.zygne.data.domain.model.*;
import com.zygne.data.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.List;

public class BiasInteractorImpl extends BaseInteractor implements BiasInteractor {

    private final List<Histogram> histogramList;
    private final Callback callback;

    public BiasInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList) {
        super(executor, mainThread);
        this.histogramList = histogramList;
        this.callback = callback;
    }

    @Override
    public void run() {
        histogramList.sort(new Histogram.TimeComparator());

        int yearToFetch = TimeHelper.getYearFromTimeStamp(histogramList.get(0).timeStamp);

        List<List<Histogram>> histogramsByYear = new ArrayList<>();
        List<Histogram> histogramYear = new ArrayList<>();

        for (Histogram histogram : histogramList) {
            int year = TimeHelper.getYearFromTimeStamp(histogram.timeStamp);
            if (year == yearToFetch) {
                histogramYear.add(histogram);
            } else {
                yearToFetch++;
                histogramsByYear.add(histogramYear);
                histogramYear = new ArrayList<>();
                histogramYear.add(histogram);
            }
        }

        histogramsByYear.remove(0);

        List<Bias> biasList = getChange(histogramsByYear);

        FileWriter fileWriter = new FileWriter("bias");

        String line = histogramsByYear.size() + "years :";

        for(Bias b : biasList){
            line += b.change + ",";
        }
        fileWriter.writeLine(line);
        fileWriter.close();

        mainThread.post(() -> callback.onBiasCreated(biasList));
    }

    private List<Bias> getChange(List<List<Histogram>> histogramsByYear) {
        List<Bias> biasList = new ArrayList<>();

        int length = histogramsByYear.size();

        for (int i = 0; i < 12; i++) {

            double sum = 0.0d;
            for (List<Histogram> items : histogramsByYear) {
                sum += getPercentChange(items.get(i).open, items.get(i).close);
            }

            Bias b = new Bias();
            b.change = (sum / length);
            b.index = i;
            biasList.add(b);
        }

       return biasList;
    }


    private double getPercentChange(double start, double end) {
        return ((end - start) / start) * 100d;
    }

}