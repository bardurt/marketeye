package com.zygne.data.domain.model;

public class DataSize {

    private final int size;
    private final Unit unit;

    public DataSize(int size, Unit unit) {
        this.size = size;
        this.unit = unit;
    }

    public int getSize() {
        return size;
    }

    public Unit getUnit() {
        return unit;
    }

    public enum Unit {
        Day("Day"),
        Week("Week"),
        Month("Month"),
        Year("Year");

        private final String label;

        Unit(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

}
