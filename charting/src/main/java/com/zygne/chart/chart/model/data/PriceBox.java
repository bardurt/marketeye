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

    public double getMid() {
        return (end + start) / 2;
    }

    public double getPercentile(int percent) {

        if (percent == 0) {
            return start;
        }

        if (percent == 100) {
            return end;
        }

        double fraction = percent / 100d;

        double change = (start - end) * fraction;

        return start + change;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public boolean inside(double value) {
        return (value >= start && value <= end);
    }
}
