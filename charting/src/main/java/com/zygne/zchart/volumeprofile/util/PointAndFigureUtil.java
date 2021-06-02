package com.zygne.zchart.volumeprofile.util;

import com.zygne.zchart.volumeprofile.menu.PriceBoard;
import com.zygne.zchart.volumeprofile.model.chart.PriceLine;
import com.zygne.zchart.volumeprofile.model.data.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PointAndFigureUtil {

    private static final int boxHeight = 5;
    private static final int boxWidth = 10;

    public static List<PointAndFigure> createPnF() {

        List<PointAndFigure> pnfList = new ArrayList<>();


        PointAndFigure pnf1 = new PointAndFigure();
        pnf1.setState(PointAndFigure.BULLISH_BOX);
        pnf1.setX(boxWidth);
        pnf1.setY(100);
        pnf1.setWidth(boxWidth);
        pnf1.setHeight(boxHeight);
        pnf1.setPrice(95);

        pnfList.add(pnf1);

        PointAndFigure pnf2 = new PointAndFigure();
        pnf2.setState(PointAndFigure.BULLISH_BOX);
        pnf2.setX(boxWidth);
        pnf2.setY(100 - boxHeight);
        pnf2.setWidth(boxWidth);
        pnf2.setHeight(boxHeight);
        pnf2.setPrice(96);

        pnfList.add(pnf2);


        PointAndFigure pnf3 = new PointAndFigure();
        pnf3.setState(PointAndFigure.BULLISH_BOX);
        pnf3.setX(boxWidth);
        pnf3.setY(100 - boxHeight * 2);
        pnf3.setWidth(boxWidth);
        pnf3.setHeight(boxHeight);
        pnf3.setPrice(97);

        pnfList.add(pnf3);

        int end = pnf3.getRight();


        PointAndFigure pnf4 = new PointAndFigure();
        pnf4.setState(PointAndFigure.BEARISH_BOX);
        pnf4.setX(end);
        pnf4.setY(100 - boxHeight);
        pnf4.setWidth(boxWidth);
        pnf4.setHeight(boxHeight);
        pnf4.setPrice(96);

        pnfList.add(pnf4);

        PointAndFigure pnf5 = new PointAndFigure();
        pnf5.setState(PointAndFigure.BEARISH_BOX);
        pnf5.setX(end);
        pnf5.setY(100);
        pnf5.setWidth(boxWidth);
        pnf5.setHeight(boxHeight);
        pnf5.setPrice(95);

        pnfList.add(pnf5);

        PointAndFigure pnf6 = new PointAndFigure();
        pnf6.setState(PointAndFigure.BEARISH_BOX);
        pnf6.setX(end);
        pnf6.setY(100 + boxHeight);
        pnf6.setWidth(boxWidth);
        pnf6.setHeight(boxHeight);
        pnf6.setPrice(94);

        pnfList.add(pnf6);

        return pnfList;
    }

    public static void adjustPnfToPriceBoard(List<PointAndFigure> pointAndFigures, PriceBoard priceBoard) {

        for (PointAndFigure pointAndFigure : pointAndFigures) {

            PriceLine pb = priceBoard.getPriceLevel(pointAndFigure.getPrice());

            if (pb != null) {

                pointAndFigure.setY(pb.getY());
                pointAndFigure.setX(pointAndFigure.getColumn() * boxWidth);
                pointAndFigure.setHeight(pb.getHeight());
            }

        }

    }

    public static List<PointAndFigure> createPnF2(List<PnfItem> items, double boxSize) {
        List<PointAndFigure> pnfList = new ArrayList<>();

        int column = 10;

        PnfItem e0 = items.get(0);
        PointAndFigure firstitem = new PointAndFigure();
        firstitem.setColumn(column);
        firstitem.setPrice(e0.getPrice());
        firstitem.setBoxCount(e0.getNumBoxes());
        firstitem.setWidth(boxWidth);
        firstitem.setHeight(boxHeight);

        if(e0.isXBox()){
            firstitem.setState(PointAndFigure.BULLISH_BOX);
        } else {
            firstitem.setState(PointAndFigure.BEARISH_BOX);
        }

        pnfList.add(firstitem);


        for(int i = 1; i < items.size(); i++){

            PnfItem e = items.get(i);

            if(e.isXBox() && !e0.isXBox()){
                column++;
            }

            if(!e.isXBox() && e0.isXBox()){
                column++;
            }

            PointAndFigure pointAndFigure = new PointAndFigure();
            pointAndFigure.setColumn(column);
            pointAndFigure.setPrice(e.getPrice());
            pointAndFigure.setBoxCount(e.getNumBoxes());
            pointAndFigure.setWidth(boxWidth);
            pointAndFigure.setHeight(boxHeight);

            if(e.isXBox()){
                pointAndFigure.setState(PointAndFigure.BULLISH_BOX);
            } else {
                pointAndFigure.setState(PointAndFigure.BEARISH_BOX);
            }

            pnfList.add(pointAndFigure);



            e0 = e;

        }


        return pnfList;
    }

    public static List<PointAndFigureBox> createPnfBoxes(List<PointAndFigure> data, double boxSize) {
        List<PointAndFigureBox> pnfList = new ArrayList<>();

        if (data.isEmpty()) {
            return pnfList;
        }


        int currentColumn = -1;

        PointAndFigureBox pointAndFigureBox = null;
        for (PointAndFigure e : data) {

            if (e.getColumn() == currentColumn) {
                if (pointAndFigureBox != null) {
                    pointAndFigureBox.getItems().add(e);
                }
            } else {
                if (pointAndFigureBox != null) {
                    completeBox(pointAndFigureBox, boxSize);
                    pnfList.add(pointAndFigureBox);
                } else {
                    pointAndFigureBox = new PointAndFigureBox();
                    pointAndFigureBox.setColumn(e.getColumn());
                }

            }

            currentColumn = e.getColumn();

        }

        return pnfList;
    }

    public static void completeBox(PointAndFigureBox pointAndFigureBox, double boxSize){

        if(pointAndFigureBox.getItems().isEmpty()){
            return;
        }

        if(pointAndFigureBox.getItems().size() < 2){
            return;
        }

        Collections.sort(pointAndFigureBox.getItems(), new PointAndFigure.PriceComparator());

        int size = pointAndFigureBox.getItems().size();
        int column = pointAndFigureBox.getColumn();
        int state = pointAndFigureBox.getItems().get(0).getState();
        double minValue = pointAndFigureBox.getItems().get(0).getPrice();
        double maxValue = pointAndFigureBox.getItems().get(size-1).getPrice();


        pointAndFigureBox.getItems().clear();

        double price = minValue;

        while (price <= maxValue){
            PointAndFigure pointAndFigure = new PointAndFigure();
            pointAndFigure.setColumn(column);
            pointAndFigure.setPrice(price);
            pointAndFigure.setState(state);
            pointAndFigure.setWidth(boxWidth);
            pointAndFigure.setHeight(boxHeight);

            pointAndFigureBox.getItems().add(pointAndFigure);
            price+= boxSize;
        }
    }
}
