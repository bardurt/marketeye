package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.GapBiasInteractor;
import com.zygne.stockalyze.domain.model.GapResult;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

import static com.zygne.stockalyze.domain.utils.Constants.MAX_PROBABILITY;

public class GapBiasInteractorImpl implements GapBiasInteractor {

    private static final double MAX_BIAS = 2;
    private static final double MAX_SKEW = 0.10;

    private final Callback callback;
    private final List<Node> data;
    private GapResult gapResult;

    public GapBiasInteractorImpl(Callback callback, List<Node> data, GapResult gapResult) {
        this.callback = callback;
        this.data = data;
        this.gapResult = gapResult;
    }

    @Override
    public void execute() {


        double slpwDpwn = 0.02;
        double upperBias = 1.00;

        if (gapResult.openCloseRange.avg > 10.0) {
            upperBias += 0.2;
        }

        if (gapResult.openHighRange.avg > 10.0) {
            upperBias += 0.2;
        }

        if (upperBias > 1 + MAX_SKEW) {
            upperBias = 1 + MAX_SKEW;
        }

        double lowerBias = MAX_BIAS - upperBias;

        int originIndex = 0;

        for (int i = 0; i < data.size(); i++) {

            if (data.get(i).origin) {
                originIndex = i;
                data.get(i).probability = 100;
            }
        }

        List<Node> upperPull = new ArrayList<>();

        for (int i = originIndex - 1; i > -1; i--) {
            upperPull.add(data.get(i));
        }

        List<Node> lowerPull = new ArrayList<>();

        for (int i = originIndex + 1; i < data.size(); i++) {
            lowerPull.add(data.get(i));
        }

        createBias(upperPull, upperBias, slpwDpwn, 1);
        createBias(lowerPull, lowerBias, slpwDpwn, 0.9);

        List<Node> filteredList = new ArrayList<>();

        filteredList.addAll(data);

        callback.onGapBiasCreated(data);

    }

    private void createBias(List<Node> nodes, double bias, double slowDown, double minBias) {

        for (Node n : nodes) {
            n.probability *= bias;
            bias *= (1 - slowDown);

            if (bias < minBias) {
                bias = minBias;
            }

            if (n.probability >= MAX_PROBABILITY) {
                n.probability = MAX_PROBABILITY;
            } else if (n.probability < 1) {
                n.probability = 1;
            }
        }
    }
}
