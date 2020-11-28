package com.zygne.stockanalyzer.domain.model;

public class CandleStick {

    public final boolean bullish;
    public final double bodySize;
    public final double lowerWick;
    public final double upperWick;
    public final double top;
    public final double bottom;
    public final double bodyTop;
    public final double bodyBottom;
    public final long timeStamp;
    public final long volume;

    public CandleStick(Histogram histogram){
        bullish = histogram.close > histogram.open;
        bodySize = Math.abs(histogram.close - histogram.open);
        top = histogram.high;
        bottom = histogram.low;

        if(bullish){
            upperWick = histogram.high - histogram.close;
            lowerWick = histogram.open - histogram.low;
            bodyTop = histogram.close;
            bodyBottom = histogram.open;
        } else {
            upperWick = histogram.high - histogram.open;
            lowerWick = histogram.close - histogram.low;
            bodyTop = histogram.open;
            bodyBottom = histogram.close;
        }

        timeStamp = histogram.timeStamp;
        volume = histogram.volume;
    }

    public double getSize(){
        return top - bottom;
    }

}
