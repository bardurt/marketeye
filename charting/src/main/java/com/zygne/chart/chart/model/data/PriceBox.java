package com.zygne.chart.chart.model.data;

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

    public double getMid(){
        return (end + start)/2;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public boolean inside(double value){
        return (value >= start && value <= end);
    }
}
