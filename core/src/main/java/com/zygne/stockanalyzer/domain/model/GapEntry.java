package com.zygne.stockanalyzer.domain.model;

public class GapEntry {

    private double openHighChange;
    private double openCloseChange;


    public double getOpenHighChange() {
        return openHighChange;
    }

    public void setOpenHighChange(double openHighChange) {
        this.openHighChange = openHighChange;
    }

    public double getOpenCloseChange() {
        return openCloseChange;
    }

    public void setOpenCloseChange(double openCloseChange) {
        this.openCloseChange = openCloseChange;
    }

    public boolean bullish(){
        return openCloseChange > 0;
    }
}
