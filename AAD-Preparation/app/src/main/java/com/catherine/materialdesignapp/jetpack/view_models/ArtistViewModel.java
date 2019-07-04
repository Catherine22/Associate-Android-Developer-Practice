package com.catherine.materialdesignapp.jetpack.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.catherine.materialdesignapp.jetpack.entities.Artist;
import com.catherine.materialdesignapp.jetpack.repositories.ArtistRepository;

public class ArtistViewModel extends ViewModel {

    private ArtistRepository mArtistRepository;
    private LiveData<PagedList<Artist>> mArtistLiveData;
    private final MediatorLiveData<PagedList<Artist>> mArtistMediator = new MediatorLiveData<>();
    private int pageSize;


    public ArtistViewModel(ArtistRepository mArtistRepository) {
        this.mArtistRepository = mArtistRepository;
        pageSize = mArtistRepository.count();
        mArtistLiveData = mArtistRepository.getArtistLiveData(pageSize);
        mArtistMediator.addSource(mArtistLiveData, mArtistMediator::setValue);
    }

    public MediatorLiveData<PagedList<Artist>> getArtistLiveData() {
        return mArtistMediator;
    }

    public void search(String keyword) {
        mArtistMediator.removeSource(mArtistLiveData);
        mArtistLiveData = mArtistRepository.getArtistLiveData(keyword, pageSize);
        mArtistMediator.addSource(mArtistLiveData, mArtistMediator::setValue);
    }

    public void insert(Artist artist) {
        mArtistRepository.insert(artist);
    }
}
