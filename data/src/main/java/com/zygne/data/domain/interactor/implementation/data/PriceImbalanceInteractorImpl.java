package com.zygne.data.domain.interactor.implementation.data;


import com.zygne.data.domain.interactor.implementation.data.base.PriceImbalanceInteractor;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.PriceImbalance;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;

import java.util.ArrayList;
import java.util.List;

public class PriceImbalanceInteractorImpl extends BaseInteractor implements PriceImbalanceInteractor {

    private Callback callback;
    private List<Histogram> data;

    public PriceImbalanceInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void run() {

        List<PriceImbalance> imbalances = findImbalances(data);

        //findFilled(imbalances, data);

        List<PriceImbalance> openImbalances = new ArrayList<>();

        for (PriceImbalance p : imbalances) {
            openImbalances.add(p);
        }

        mainThread.post(() -> callback.onPriceImbalanceCompleted(openImbalances));
    }

    private List<PriceImbalance> findImbalances(List<Histogram> histograms) {
        histograms.sort(new Histogram.TimeComparator());
        List<PriceImbalance> priceImbalances = new ArrayList<>();


        for (int i = 1; i < histograms.size() - 1; i++) {

            Histogram prev = histograms.get(i - 1);
            Histogram current = histograms.get(i);
            Histogram next = histograms.get(i + 1);

            if (prev.low > next.high) {
                PriceImbalance p = new PriceImbalance();
                p.setEnd(prev.low);
                p.setStart(next.high);
                p.setTimeStamp(current.timeStamp);
                p.setIndex(i);

                if (current.inBody(p.getMidPoint())) {
                    priceImbalances.add(p);
                }
            }

            if (prev.high < next.low) {
                PriceImbalance p = new PriceImbalance();
                p.setEnd(next.low);
                p.setStart(prev.high);
                p.setTimeStamp(current.timeStamp);
                p.setIndex(i);

                if (current.inBody(p.getMidPoint())) {
                    priceImbalances.add(p);
                }
            }

        }


        return priceImbalances;
    }

    private void findFilled(List<PriceImbalance> imbalances, List<Histogram> histograms) {

        histograms.sort(new Histogram.TimeComparator());

        for (PriceImbalance g : imbalances) {

            for (int i = g.getIndex() + 1; i < histograms.size(); i++) {

                Histogram h = histograms.get(i);

                if (h.contains(g.getStart())) {
                    g.setFilled(true);
                    break;
                }

                if (h.contains(g.getEnd())) {
                    g.setFilled(true);
                    break;
                }

                if (h.contains(g.getMidPoint())) {
                    g.setFilled(true);
                    break;
                }

            }
        }

    }
}
