package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.GapHistoryInteractor;
import com.zygne.stockanalyzer.domain.model.GapEntry;
import com.zygne.stockanalyzer.domain.model.GapHistory;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.utils.NumberHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GapHistoryInteractorImpl extends BaseInteractor implements GapHistoryInteractor {

    private static final double MIN_CHANGE = 10;

    private Callback callback;
    private List<Histogram> histogramList;

    public GapHistoryInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
    }

    @Override
    public void run() {

        List<GapEntry> gapEntries = getGapDetails(findGaps(histogramList));


        GapHistory gapHistory = new GapHistory();

        double bullishChangeSum = 0;
        double bearishChangeSum = 0;
        double highChangeSum = 0;
        double maxHigh = 0;
        int bullishGaps = 0;
        int bearishGaps = 0;
        int size = gapEntries.size();

        for (GapEntry gapEntry : gapEntries) {

            highChangeSum += gapEntry.getOpenHighChange();

            if (gapEntry.getOpenHighChange() > maxHigh) {
                maxHigh = gapEntry.getOpenHighChange();
            }

            if (gapEntry.bullish()) {
                bullishChangeSum += gapEntry.getOpenCloseChange();
                bullishGaps++;
            } else {
                bearishChangeSum += gapEntry.getOpenCloseChange();
                bearishGaps++;
            }
        }

        gapHistory.setTotalGaps(gapEntries.size());
        gapHistory.setMinChange(MIN_CHANGE);
        gapHistory.setBullishGaps(bullishGaps);
        gapHistory.setAvgBullishChange(bullishChangeSum / bullishGaps);
        gapHistory.setAvgBearishChange(bearishChangeSum / bearishGaps);
        gapHistory.setAvgHighChange(highChangeSum / size);
        gapHistory.setMaxHighChange(maxHigh);


        mainThread.post(() -> callback.onGapHistoryCompleted(gapHistory));
    }

    private List<Histogram> findGaps(List<Histogram> histograms) {

        List<Histogram> gaps = new ArrayList<>();

        Collections.sort(histograms, new Histogram.TimeComparator());
        Collections.reverse(histograms);

        for (int i = 0; i < histograms.size() - 1; i++) {
            Histogram h1 = histograms.get(i);
            Histogram h2 = histograms.get(i + 1);


            if (h2.open > h1.close) {
                double change = NumberHelper.getPercentChange(h1.close, h2.open);

                if (change >= MIN_CHANGE) {
                    gaps.add(h2);
                }
            }

        }

        return gaps;
    }

    private List<GapEntry> getGapDetails(List<Histogram> histograms) {

        List<GapEntry> entries = new ArrayList<>();

        for (Histogram h : histograms) {

            GapEntry gapEntry = new GapEntry();

            gapEntry.setOpenCloseChange(NumberHelper.getPercentChange(h.open, h.close));
            gapEntry.setOpenHighChange(NumberHelper.getPercentChange(h.open, h.high));

            entries.add(gapEntry);

        }

        return entries;
    }

}
