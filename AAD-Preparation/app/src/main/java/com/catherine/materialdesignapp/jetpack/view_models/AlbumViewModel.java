package com.catherine.materialdesignapp.jetpack.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.catherine.materialdesignapp.jetpack.entities.Album;
import com.catherine.materialdesignapp.jetpack.repositories.AlbumRepository;

public class AlbumViewModel extends ViewModel {

    private AlbumRepository mAlbumRepository;
    private LiveData<PagedList<Album>> albumLiveData;
    private final MediatorLiveData<PagedList<Album>> mAlbumMediator = new MediatorLiveData<>();
    private int pageSize;


    public AlbumViewModel(AlbumRepository mAlbumRepository) {
        this.mAlbumRepository = mAlbumRepository;
        pageSize = mAlbumRepository.count();
        albumLiveData = mAlbumRepository.getAlbumLiveData(pageSize);
        mAlbumMediator.addSource(albumLiveData, mAlbumMediator::setValue);
    }

    public MediatorLiveData<PagedList<Album>> getAlbumLiveData() {
        return mAlbumMediator;
    }

    public void search(String keyword) {
        mAlbumMediator.removeSource(albumLiveData);
        albumLiveData = mAlbumRepository.getAlbumLiveData(keyword, pageSize);
        mAlbumMediator.addSource(albumLiveData, mAlbumMediator::setValue);
    }

    public void insert(Album album) {
        mAlbumRepository.insert(album);
    }
}
