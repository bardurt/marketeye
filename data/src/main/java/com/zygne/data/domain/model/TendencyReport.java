package com.zygne.data.domain.model;

import java.util.List;

public record TendencyReport(
        List<Tendency> tendencies
) {

    public long getQuarter(int quarter) {
        Tendency tendency = tendencies.get(tendencies.size() - 1);

        int length = tendency.data.size();

        double scalar = switch (quarter) {
            case 1 -> 0.25;
            case 2 -> 0.5;
            case 3 -> 0.75;
            default -> 1;
        };

        return tendency.data.get((int) (length * scalar)).timeStamp;
    }


}