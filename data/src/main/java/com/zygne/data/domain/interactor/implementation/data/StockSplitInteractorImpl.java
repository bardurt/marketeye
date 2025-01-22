package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.BaseInteractor;
import com.zygne.data.domain.model.Histogram;

import java.util.ArrayList;
import java.util.List;

public class StockSplitInteractorImpl extends BaseInteractor implements StockSplitInteractor {

    private final Callback callback;
    private final List<Histogram> histograms;
    private final Logger logger;

    public StockSplitInteractorImpl(Executor executor, MainThread mainThread, Callback callback, Logger logger, List<Histogram> histograms) {
        super(executor, mainThread);
        this.callback = callback;
        this.histograms = histograms;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            List<Histogram> adjustedHistograms = detectAndAdjustSplits(histograms);
            mainThread.post(() -> callback.onStockSplitsDetected(adjustedHistograms));
        } catch (Exception e) {
            logger.log(Logger.LOG_LEVEL.ERROR, "Unexpected error in run: " + e.getMessage());
        }
    }

    private List<Histogram> detectAndAdjustSplits(List<Histogram> histograms) {
        List<Histogram> adjustedHistograms = new ArrayList<>();
        double adjustmentFactor = 1.0;

        System.out.println("First item " + histograms.get(0).timeStamp);
        adjustedHistograms.add(histograms.get(0));
        for (int i = 0; i < histograms.size()-1; i++) {
            Histogram current = histograms.get(i);
            Histogram previous = histograms.get(i+1);

            double priceRatio = current.open / previous.close;
            if (isStockSplit(priceRatio)) {
                adjustmentFactor *= priceRatio;
                logger.log(Logger.LOG_LEVEL.INFO, "Stock split detected on " + current.dateTime + " with ratio: " + priceRatio);
                System.out.println("Stock split detected on " + current.dateTime + " with ratio: " + priceRatio);
            }

            // Adjust prices and volumes based on the cumulative adjustment factor
            Histogram adjustedHistogram = adjustHistogram(previous, adjustmentFactor);
            adjustedHistograms.add(adjustedHistogram);
        }

        return adjustedHistograms;
    }

    private boolean isStockSplit(double priceRatio) {
        // Common forward split ratios (e.g., 2:1, 3:1)
        double[] forwardRatios = {2.0, 3.0, 1.5, 4.0, 5.0, 10.0, 1.25, 1.333};
        // 1:2, 1:3, 1:4, 1:5, 1:6, 1:8, 1:10
        double[] reverseRatios = {0.5, 0.333, 0.25, 0.2, 0.166, 0.125, 0.1}; // Represent 1:2, 1:3, 1:4, 1:5, 1:8, 1:10

        // Check forward split ratios
        for (double ratio : forwardRatios) {
            if (Math.abs(priceRatio - ratio) < 0.1) {
                return true;
            }
        }

        // Check reverse split ratios
        for (double ratio : reverseRatios) {
            if (Math.abs(priceRatio - ratio) < 0.025) {
                return true;
            }
        }

        return false;
    }

    private Histogram adjustHistogram(Histogram histogram, double adjustmentFactor) {
        Histogram adjusted = new Histogram();
        adjusted.dateTime = histogram.dateTime;
        adjusted.timeStamp = histogram.timeStamp;
        adjusted.open = histogram.open * adjustmentFactor;
        adjusted.high = histogram.high * adjustmentFactor;
        adjusted.low = histogram.low * adjustmentFactor;
        adjusted.close = histogram.close * adjustmentFactor;
        adjusted.volume = histogram.volume; // Volume remains unchanged
        adjusted.volumeSma = histogram.volumeSma; // SMA calculation is context-dependent
        adjusted.volumeSmaPercentile = histogram.volumeSmaPercentile; // Percentile calculation is unchanged
        return adjusted;
    }
}