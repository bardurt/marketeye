package com.zygne.data;

import com.zygne.data.domain.model.Asset;

import java.util.ArrayList;
import java.util.List;

public class Assets {

    public static final List<Asset> assetList = new ArrayList<Asset>() {
        {
            add(new Asset("S&P 500 E-Mini", "%5EGSPC", Asset.Classification.FUTURE));
            add(new Asset("Nasdaq E-Mini", "%5EIXIC", Asset.Classification.FUTURE));
            add(new Asset("Gold", "GC=F", Asset.Classification.FUTURE));
            add(new Asset("Silver", "SI=F", Asset.Classification.FUTURE));
            add(new Asset("Crude Oil", "CL=F", Asset.Classification.FUTURE));
            add(new Asset("Wheat", "KE=F", Asset.Classification.FUTURE));
            add(new Asset("USD", "DX-Y.NYB", Asset.Classification.FUTURE));
        }
    };

}
