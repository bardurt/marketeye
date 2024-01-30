package com.zygne.data.domain.model;

public class GapHistory {

    private double minChange;
    private double totalGaps;
    private double bullishGaps;
    private double avgBullishChange;
    private double avgBearishChange;
    private double avgHighChange;
    private double maxHighChange;

    public double getMinChange() {
        return minChange;
    }

    public void setMinChange(double minChange) {
        this.minChange = minChange;
    }

    public double getTotalGaps() {
        return totalGaps;
    }

    public void setTotalGaps(double totalGaps) {
        this.totalGaps = totalGaps;
    }

    public double getBullishGaps() {
        return bullishGaps;
    }

    public void setBullishGaps(double bullishGaps) {
        this.bullishGaps = bullishGaps;
    }

    public double getAvgBullishChange() {
        return avgBullishChange;
    }

    public void setAvgBullishChange(double avgBullishChange) {
        this.avgBullishChange = avgBullishChange;
    }

    public double getAvgBearishChange() {
        return avgBearishChange;
    }

    public void setAvgBearishChange(double avgBearishChange) {
        this.avgBearishChange = avgBearishChange;
    }

    public double getAvgHighChange() {
        return avgHighChange;
    }

    public void setAvgHighChange(double avgHighChange) {
        this.avgHighChange = avgHighChange;
    }

    public double getMaxHighChange() {
        return maxHighChange;
    }

    public void setMaxHighChange(double maxHighChange) {
        this.maxHighChange = maxHighChange;
    }

    @Override
    public String toString() {
        return "Gaps " + totalGaps + ", Bullish " + bullishGaps + ", Avg Bullish " + avgBullishChange
                + ", Bearish " + (totalGaps - bullishGaps) + " Avg Bearish " + avgBearishChange
                + ", Avg High " + avgHighChange  + ", Max High " + maxHighChange;
    }
}
