package com.catherine.materialdesignapp.jetpack.view_models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.catherine.materialdesignapp.jetpack.entities.Album;
import com.catherine.materialdesignapp.jetpack.repositories.AlbumRepository;

import java.util.List;

public class AlbumViewModel extends AndroidViewModel {

    private AlbumRepository mAlbumRepository;
    private LiveData<List<Album>> albumLiveData;


    public AlbumViewModel(Application application) {
        super(application);
        mAlbumRepository = new AlbumRepository(application);
        albumLiveData = mAlbumRepository.getAlbumLiveData();
    }

    public LiveData<List<Album>> getAlbumLiveData() {
        return albumLiveData;
    }

    public void insert(Album album) {
        mAlbumRepository.insert(album);
    }

    public void release() {
        mAlbumRepository.releaseFirebase();
    }

}
