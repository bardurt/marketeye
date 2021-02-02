package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.VolumeBarDetails;

import java.util.List;

public interface VolumeBarDetailsInteractor extends Interactor {

    interface Callback{
        void onVolumeBarDetailsCreated(List<VolumeBarDetails> data);
    }
}
