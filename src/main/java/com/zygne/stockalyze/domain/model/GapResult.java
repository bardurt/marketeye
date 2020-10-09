package com.zygne.stockalyze.domain.model;

public class GapResult {

    public double gapBull = 0;
    public int gapCount = 0;
    public double gapLimit = 0;
    public OpenCloseRange openCloseRange = new OpenCloseRange();
    public LowHighRange lowHighRange = new LowHighRange();
    public OpenHighRange openHighRange = new OpenHighRange();

    public class OpenHighRange {
        public double max = 0;
        public double min = 0;
        public double avg = 0;
        public double sum = 0;
        public double count = 0;

        public void calculateAverage(){
            avg = sum / count;
        }
    }

    public class OpenCloseRange {
        public double max = 0;
        public double min = 0;
        public double avg = 0;
        public double sum = 0;
        public double count = 0;

        public void calculateAverage(){
            avg = sum / count;
        }
    }

    public class LowHighRange {
        public double max = 0;
        public double min = 0;
        public double avg = 0;
        public double sum = 0;
        public double count = 0;

        public void calculateAverage(){
            avg = sum / count;
        }
    }

}
