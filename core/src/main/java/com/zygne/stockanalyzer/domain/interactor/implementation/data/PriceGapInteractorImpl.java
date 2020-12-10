package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.PriceGapInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.PriceGap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceGapInteractorImpl extends BaseInteractor implements PriceGapInteractor {

    private static final double MIN_CHANGE = 3d;

    private Callback callback;
    private List<Histogram> histogramList;

    public PriceGapInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = data;
    }

    @Override
    public void run() {

        List<PriceGap> gaps = findGaps(histogramList);

        findFilled(gaps, histogramList);

        List<PriceGap> openGaps = new ArrayList<>();

        for (PriceGap p : gaps) {
            if (!p.isFilled()) {
                openGaps.add(p);
            }
        }

        openGaps.sort(new PriceGap.TimeComparator());
        Collections.reverse(openGaps);

        mainThread.post(() -> callback.onPriceGapsFound(openGaps));
    }

    private List<PriceGap> findGaps(List<Histogram> data) {
        List<PriceGap> gaps = new ArrayList<>();

        data.sort(new Histogram.TimeComparator());

        for (int i = 0; i < data.size() - 1; i++) {

            Histogram h1 = data.get(i);
            Histogram h2 = data.get(i + 1);

            if (h2.low > h1.high) {
                double change = ((h2.low - h1.high) / h1.low) * 100;

                if (change > MIN_CHANGE) {
                    PriceGap priceGap = new PriceGap();
                    priceGap.setOuterStart(h1.close);
                    priceGap.setOuterEnd(h2.open);
                    priceGap.setInnerStart(h1.high);
                    priceGap.setInnerEnd(h2.low);
                    priceGap.setIndex(i + 1);
                    priceGap.setTimeStamp(h2.timeStamp);

                    gaps.add(priceGap);
                }
            } else if (h2.high < h1.low) {
                double change = ((h1.low - h2.high) / h2.low) * 100;

                if (change > MIN_CHANGE) {
                    PriceGap priceGap = new PriceGap();
                    priceGap.setOuterStart(h2.open);
                    priceGap.setOuterEnd(h1.close);
                    priceGap.setInnerStart(h2.high);
                    priceGap.setInnerEnd(h1.low);
                    priceGap.setIndex(i + 1);
                    priceGap.setTimeStamp(h2.timeStamp);

                    gaps.add(priceGap);
                }
            }
        }

        return gaps;
    }

    private void findFilled(List<PriceGap> gaps, List<Histogram> histograms) {

        histograms.sort(new Histogram.TimeComparator());

        for (PriceGap g : gaps) {

            for (int i = g.getIndex() + 1; i < histograms.size(); i++) {

                Histogram h = histograms.get(i);

                if (h.intersects(g.getMidPoint())) {
                    g.setFilled(true);
                    break;
                }

            }
        }

    }
}
