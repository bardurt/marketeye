package com.zygne.chart.chart.menu;

import com.zygne.chart.chart.Canvas;
import com.zygne.chart.chart.model.chart.Object2d;
import com.zygne.chart.chart.model.chart.PriceLine;

import java.util.ArrayList;
import java.util.List;

public class PriceBoard extends Object2d{

    private static final String TAG = "PriceNBoard";

    private double maxValue = 100;
    private double minValue = 0;
    private double priceChange = 1;
    int barSize = 10;

    private List<PriceLine> priceLines = new ArrayList<>();

    public PriceBoard(double maxValue, double minValue, double priceChange) {
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.priceChange = priceChange;
    }

    public void init(){
        priceLines.clear();
        int increment = 0;

        double price = maxValue+increment;

        while (price >= minValue ){
            if(price - priceChange < 0){
                break;
            }

            increment+= barSize;
            PriceLine priceLine = new PriceLine();
            priceLine.getPriceBox().setEnd(price);
            priceLine.getPriceBox().setStart(price - priceChange);
            priceLine.setWidth(width);
            priceLine.setX(x);
            priceLine.setHeight(barSize);
            priceLine.setY(increment);

            priceLines.add(priceLine);
            price -= priceChange;

        }

    }

    public List<PriceLine> getObject2dList() {
        return priceLines;
    }

    public PriceLine getPriceLevel(double price) {

        PriceLine priceLine = null;

        for(PriceLine e : priceLines){
            if(e.getPriceBox().inside(price)){
                priceLine = e;
                break;
            }
        }

        return priceLine;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        for(Object2d p : priceLines){
            p.setX(x);
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
