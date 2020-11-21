package com.zygne.stockalyze.domain.model;

import java.util.Comparator;

public class Node {

    public double level;
    public int pull;
    public boolean origin;
    public double probability;
    public double change;
    public double prediction;
    public String note = "";
    public int side = 0;

    public double getLevel() {
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

    public static final class LevelComparator implements Comparator<Node>{
        @Override
        public int compare(Node o1, Node o2) {
            return Double.compare(o1.level, o2.level);
        }
    }

}
