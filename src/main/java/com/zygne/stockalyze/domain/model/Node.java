package com.zygne.stockalyze.domain.model;

import java.util.Comparator;

public class Node implements Comparable {

    public int level;
    public int pull;
    public boolean origin;
    public double probability;
    public double change;
    public double prediction;
    public double strength;
    public String note = "";
    public int side = 0;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPull() {
        return pull;
    }

    public void setPull(int pull) {
        this.pull = pull;
    }

    public boolean isOrigin() {
        return origin;
    }

    public void setOrigin(boolean origin) {
        this.origin = origin;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    @Override
    public int compareTo(Object o) {
        int levelB = ((Node)o).level;

        if(level < levelB){
            return 1;
        } else if(level == levelB){
            return 0;
        }

        return -1;
    }

    public static final class StrengthComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return Double.compare(o1.strength, o2.strength);
        }
    }

}
