
package com.zygne.zchart.chart.model.data;

public class PnfItem {

   /**
    * numBoxes is how many boxes to get to current price level
    */
   private double price;
   private int numBoxes;

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public int getNumBoxes() {
      return numBoxes;
   }

   public void setNumBoxes(int numBoxes) {
      this.numBoxes = numBoxes;
   }

   public boolean isXBox() {
      return numBoxes >= 0;
   }

   public String toString() {
      return "Price " + price + ", boxes " + numBoxes;
   }
}
