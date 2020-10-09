package com.zygne.stockalyze.ui.fx;

import com.zygne.stockalyze.ResourceLoader;

public abstract class Scene {

    protected ResourceLoader resourceLoader;

    public Scene(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public abstract void show();
}
