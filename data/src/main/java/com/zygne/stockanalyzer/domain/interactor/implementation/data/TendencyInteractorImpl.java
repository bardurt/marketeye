package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.TendencyInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.TendencyEntry;
import com.zygne.stockanalyzer.domain.model.TendencyReport;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;
import domain.executor.Executor;
import domain.executor.MainThread;
import domain.interactor.base.BaseInteractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TendencyInteractorImpl extends BaseInteractor implements TendencyInteractor {

    private Callback callback;
    private List<Histogram> histogramList;
    private List<Long> timeStamps = new ArrayList<>();

    public TendencyInteractorImpl(Executor executor, MainThread mainThread,
                                  Callback callback, List<Histogram> histogramList) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
    }

    @Override
    public void run() {
        Collections.sort(histogramList, new Histogram.TimeComparator());

        int endYear = TimeHelper.getYearFromTimeStamp(System.currentTimeMillis());
        int yearToFetch = TimeHelper.getYearFromTimeStamp(histogramList.get(0).timeStamp);

        List<List<Histogram>> histogramsByYear = new ArrayList<>();
        List<Histogram> currentYearOfHistogram = new ArrayList<>();

        int minLength = Integer.MAX_VALUE;

        for (int i = 0; i < histogramList.size(); i++) {
            int year = TimeHelper.getYearFromTimeStamp(histogramList.get(i).timeStamp);


            if (year == yearToFetch) {
                currentYearOfHistogram.add(histogramList.get(i));
            } else {
                yearToFetch++;
                if (year != endYear) {
                    if (minLength > currentYearOfHistogram.size()) {
                        minLength = currentYearOfHistogram.size();
                    }
                }
                histogramsByYear.add(currentYearOfHistogram);
                currentYearOfHistogram = new ArrayList<>();
            }
        }
        histogramsByYear.add(currentYearOfHistogram);

        List<List<Histogram>> normalList = new ArrayList<>();

        List<Histogram> currentYear = new ArrayList<>();
        currentYear.addAll(histogramsByYear.remove(histogramsByYear.size() - 1));
        histogramsByYear.remove(0);

        for (List<Histogram> current : histogramsByYear) {
            if (current.size() > minLength) {
                List<Histogram> data = current.subList(0, minLength - 1);
                data.add(current.get(current.size() - 1));
                normalList.add(data);
            } else {
                normalList.add(current);
            }
        }

        List<List<TendencyEntry>> avgList = new ArrayList<>();

        for (List<Histogram> histograms : normalList) {
            avgList.add(getChange(histograms));
        }

        List<TendencyEntry> currentYearAvg = getChange(currentYear);

        for (int i = 0; i < currentYearAvg.size(); i++) {
            currentYearAvg.get(i).timeStamp = avgList.get(0).get(i).timeStamp;
        }

        for(TendencyEntry h : avgList.get(0)){
            timeStamps.add(h.timeStamp);
        }

        List<TendencyEntry> fiveYearAvg = null;
        try {
            fiveYearAvg = getAverageFor(avgList, minLength, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TendencyEntry> tenYearAvg = null;

        try {
            tenYearAvg = getAverageFor(avgList, minLength, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TendencyEntry> fifteenYearAvg = null;

        try {
            fifteenYearAvg = getAverageFor(avgList, minLength, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TendencyEntry> twentyYearAvg = null;

        try {
            twentyYearAvg = getAverageFor(avgList, minLength, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TendencyReport t = new TendencyReport();
        t.currentYear = currentYearAvg;
        t.fiveYear = fiveYearAvg;
        t.tenYear = tenYearAvg;
        t.fifteenYear = fifteenYearAvg;
        t.twentyYear = twentyYearAvg;

        mainThread.post(() -> callback.omTendencyReportCreated(t));

    }

    private List<TendencyEntry> getAverageFor(List<List<TendencyEntry>> data, int minLength, int years) {
        if (data.size() < years) {
            throw new RuntimeException("Data size " + data.size() + " is less than " + years);
        }

        List<TendencyEntry> yearAvg = new ArrayList();

        int max = data.size() - 1;
        int end = max - years;

        for (int i = 0; i < minLength; i++) {
            TendencyEntry current = new TendencyEntry();
            double sum = 0.0d;
            for (int j = max; j > end; j--) {
                sum += data.get(j).get(i).avg;
            }

            sum = sum / years;
            current.timeStamp = timeStamps.get(i);
            current.avg = sum;

            yearAvg.add(current);
        }

        return yearAvg;
    }


    private List<TendencyEntry> getChange(List<Histogram> data) {
        List<TendencyEntry> avgByYearList = new ArrayList<>();
        TendencyEntry start = new TendencyEntry();
        start.avg = 0;
        start.timeStamp = data.get(0).timeStamp;
        avgByYearList.add(start);
        double startValue = data.get(0).open;
        for (int j = 1; j < data.size(); j++) {
            double endValue = data.get(j).open;
            double change = ((endValue - startValue) / startValue) * 100;

            TendencyEntry currentAvg = new TendencyEntry();
            currentAvg.avg = change;
            currentAvg.timeStamp = data.get(j).timeStamp;
            avgByYearList.add(currentAvg);

        }

        return avgByYearList;
    }

}