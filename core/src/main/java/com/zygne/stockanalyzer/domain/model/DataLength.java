package com.zygne.stockanalyzer.domain.model;

public class DataLength {

    private int size;
    private String context;

    public DataLength(int size, String context) {
        this.size = size;
        this.context = context;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
