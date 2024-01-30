/**
 *
 */
package com.zygne.chart.chart.util;

import com.zygne.chart.chart.model.data.PnfItem;
import com.zygne.chart.chart.model.data.Quote;

import java.util.ArrayList;
import java.util.List;

public class PnFCalc {
    private static int REV_SIZE = 3;

    private double _revBox;

    public double getRevBox() {
        return _revBox;
    }

    public void init(double curBox, boolean isUpReversal) {
        double revPoints = calcRevPoints(curBox, isUpReversal);
        if (isUpReversal) {
            _revBox = curBox + revPoints;
        } else {
            _revBox = curBox - revPoints;
        }

    }

    public List<PnfItem> execute(List<Quote> quotes) {
        List<PnfItem> retval = new ArrayList<>();
        boolean isXbox = true;
        double curBox = (quotes.get(0).getLow()) + PnFCalc.getBoxSize(quotes.get(0).getLow());


        if (curBox > quotes.get(0).getHigh()) {
            curBox = quotes.get(0).getLow();
        }

        int numBoxes = 0;
        for (Quote quote : quotes) {

            int numNewBoxes = 0;
            int numRevBoxes = 0;

            PnfItem pnfItem = new PnfItem();
            ;
            pnfItem.setNumBoxes(numBoxes);

            // continuation
            if (isXbox) {
                numNewBoxes = getNewXBoxes(quote.getHigh(), curBox);
            } else {
                numNewBoxes = getNewOBoxes(quote.getLow(), curBox);
            }
            if (numNewBoxes > 0) {
                if (isXbox) {
                    numBoxes += numNewBoxes;
                    curBox = getXBoxVal(numNewBoxes, curBox);
                } else {
                    numBoxes -= numNewBoxes;
                    curBox = getOBoxVal(numNewBoxes, curBox);
                }
                pnfItem.setNumBoxes(numBoxes);
                pnfItem.setPrice(curBox);
                retval.add(pnfItem);
                continue;
            }

            // reversal
            if (isXbox) {
                numRevBoxes = getNumRevOBoxes(quote.getLow(), curBox);
            } else {
                numRevBoxes = getNumRevXBoxes(quote.getHigh(), curBox);
            }
            if (numRevBoxes > 0) {
                if (isXbox) {
                    numBoxes = numRevBoxes * -1;
                    curBox = getOBoxVal(numRevBoxes, curBox);
                } else {
                    numBoxes = numRevBoxes;
                    curBox = getXBoxVal(numRevBoxes, curBox);
                }
                isXbox = !isXbox;
                pnfItem.setNumBoxes(numBoxes);
                pnfItem.setPrice(curBox);
                retval.add(pnfItem);
                continue;
            }
            pnfItem.setPrice(curBox);
            retval.add(pnfItem);
        }

        return retval;
    }

    private int getNewXBoxes(double hi, double curBox) {
        int retval = 0;
        if (hi >= curBox) {
            double boxsize = PnFCalc.getBoxSize(curBox);
            double xBox = curBox + boxsize;
            while (xBox <= hi) {
                boxsize = PnFCalc.getBoxSize(xBox);
                xBox += boxsize;
                retval++;
            }
        }
        return retval;
    }

    private double getXBoxVal(int numBoxes, double curBox) {
        double retval = curBox;
        for (int i = 0; i < numBoxes; i++) {
            double boxsize = PnFCalc.getBoxSize(retval);
            retval += boxsize;
        }
        return retval;
    }

    private double getOBoxVal(int numBoxes, double curBox) {
        double retval = curBox;
        double boxsize = PnFCalc.getBoxSize(retval);
        double priorBoxsize = boxsize;
        for (int i = 0; i < numBoxes; i++) {
            boxsize = PnFCalc.getBoxSize(retval);
            retval -= boxsize;
            if (boxsize != priorBoxsize) {
                retval += boxsize;
            }
            priorBoxsize = boxsize;
        }
        return retval;
    }

    private int getNewOBoxes(double lo, double curBox) {
        int retval = 0;
        if (lo <= curBox) {
            double boxsize = PnFCalc.getBoxSize(curBox);
            double priorBoxsize = boxsize;
            double oBox = curBox - boxsize;
            while (oBox >= lo) {
                boxsize = PnFCalc.getBoxSize(oBox);
                if (boxsize != priorBoxsize) {
                    retval++;
                }
                priorBoxsize = boxsize;
                oBox -= boxsize;
                retval++;

            }
        }
        return retval;
    }

    private int getNumRevXBoxes(double hi, double curBox) {
        int retval = 0;
        PnFCalc calc = new PnFCalc();
        calc.init(curBox, true);
        double xRevBox = calc.getRevBox();
        if (hi >= xRevBox) {
            retval = REV_SIZE;
            double boxsize = PnFCalc.getBoxSize(xRevBox);
            double xBox = xRevBox + boxsize;
            while (xBox <= hi) {
                boxsize = PnFCalc.getBoxSize(xRevBox);
                xBox += boxsize;
                retval++;
            }
        }
        return retval;
    }

    private int getNumRevOBoxes(double lo, double curBox) {
        int retval = 0;
        PnFCalc calc = new PnFCalc();
        calc.init(curBox, false);
        double oRevBox = calc.getRevBox();
        if (lo <= oRevBox) {
            retval = REV_SIZE;
            double boxsize = PnFCalc.getBoxSize(oRevBox);
            double oBox = oRevBox - boxsize;
            while (oBox >= lo) {
                boxsize = PnFCalc.getBoxSize(oRevBox);
                oBox -= boxsize;
                retval++;
            }
        }
        return retval;
    }

    private double calcRevPoints(double curBox, boolean isUpReversal) {
        double retval = 0;
        double boxSize = PnFCalc.getBoxSize(curBox);
        double box = 0;
        if (isUpReversal) {
            box = curBox;
        } else {
            box = curBox - boxSize;
        }
        for (int i = 0; i < REV_SIZE; i++) {
            boxSize = PnFCalc.getBoxSize(box);
            retval += boxSize;
            if (isUpReversal) {
                box += boxSize;
            } else {
                box -= boxSize;
            }
        }
        return retval;
    }


    public static double getBoxSize(double hilo) {
        double retval = 0d;

        if (hilo < 5) {
            retval = 0.25d;
        } else if (hilo < 20) {
            retval = 0.50d;
        } else if (hilo < 100) {
            retval = 100.0d;
        } else if (hilo < 200) {
            retval = 200.0d;
        } else if (hilo < 500) {
            retval = 500.0d;
        } else if (hilo < 1000) {
            retval = 1000.0d;
        } else {
            retval = 500.0d;
        }

        return retval;
    }

}
