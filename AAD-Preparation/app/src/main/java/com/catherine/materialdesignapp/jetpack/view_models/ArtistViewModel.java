package com.catherine.materialdesignapp.jetpack.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.jetpack.entities.Artist;
import com.catherine.materialdesignapp.jetpack.repositories.ArtistRepository;

import java.util.List;

public class ArtistViewModel extends AndroidViewModel {

    private ArtistRepository mArtistRepository;
    private LiveData<List<Artist>> artistLiveData;


    public ArtistViewModel(Application application) {
        super(application);
        mArtistRepository = new ArtistRepository(application);
        artistLiveData = mArtistRepository.getArtistLiveData();
    }

    public LiveData<List<Artist>> getArtistLiveData() {
        return artistLiveData;
    }

    public void insert(Artist artist) {
        mArtistRepository.insert(artist);
    }

}
