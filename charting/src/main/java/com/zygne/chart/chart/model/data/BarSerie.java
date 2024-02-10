package com.zygne.chart.chart.model.data;

public class BarSerie extends Serie {

    private double value;

    public BarSerie(Double value){
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
