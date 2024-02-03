package com.zygne.data.domain.model;

import java.util.List;

public class Tendency {

    public Tendency(String name, List<TendencyEntry> data) {
        this.name = name;
        this.data = data;
    }

    public String name;
    public List<TendencyEntry> data;
}
