package com.catherine.materialdesignapp.jetpack.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.catherine.materialdesignapp.jetpack.entities.Album;
import com.catherine.materialdesignapp.jetpack.repositories.AlbumRepository;

import java.util.List;

public class AlbumViewModel extends ViewModel {

    private AlbumRepository mAlbumRepository;
    private LiveData<List<Album>> albumLiveData;


    public AlbumViewModel(AlbumRepository mAlbumRepository) {
        this.mAlbumRepository = mAlbumRepository;
        albumLiveData = mAlbumRepository.getAlbumLiveData();
    }

    public LiveData<List<Album>> getAlbumLiveData() {
        return albumLiveData;
    }

    public void insert(Album album) {
        mAlbumRepository.insert(album);
    }
}
