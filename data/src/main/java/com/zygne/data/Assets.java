package com.zygne.data;

import com.zygne.data.domain.model.Asset;

import java.util.ArrayList;
import java.util.List;

public class Assets {

    public static final List<Asset> assetList = new ArrayList<>() {
        {
            add(new Asset("S&P 500 E-Mini", "%5EGSPC"));
            add(new Asset("Nasdaq E-Mini", "%5EIXIC"));
            add(new Asset("Gold", "GC=F"));
            add(new Asset("Silver", "SI=F"));
            add(new Asset("Crude Oil", "CL=F"));
            add(new Asset("Wheat", "KE=F"));
            add(new Asset("USD", "DX-Y.NYB"));
        }
    };

}
