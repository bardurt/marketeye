package com.zygne.zchart.chart.model.data;

public class PriceBox {

    private double start;
    private double end;

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public boolean inside(double value){
        return (value >= start && value <= end);
    }
}
