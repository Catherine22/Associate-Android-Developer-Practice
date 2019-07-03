package com.catherine.materialdesignapp.jetpack.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.catherine.materialdesignapp.jetpack.entities.Artist;
import com.catherine.materialdesignapp.jetpack.repositories.ArtistRepository;

import java.util.List;

public class ArtistViewModel extends ViewModel {

    private ArtistRepository mArtistRepository;
    private LiveData<List<Artist>> artistLiveData;

    public ArtistViewModel(ArtistRepository mArtistRepository) {
        this.mArtistRepository = mArtistRepository;
        artistLiveData = mArtistRepository.getArtistLiveData();
    }

    public LiveData<List<Artist>> getArtistLiveData() {
        return artistLiveData;
    }

    public void insert(Artist artist) {
        mArtistRepository.insert(artist);
    }
}
