package com.catherine.materialdesignapp.jetpack.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.repositories.PlaylistRepository;

public class PlaylistViewModel extends ViewModel {

    private PlaylistRepository mPlaylistRepository;
    private LiveData<PagedList<Playlist>> playlistLiveData;
    private final MediatorLiveData<PagedList<Playlist>> mPlaylistMediator = new MediatorLiveData<>();
    private int pageSize;


    public PlaylistViewModel(PlaylistRepository mPlaylistRepository) {
        this.mPlaylistRepository = mPlaylistRepository;
        pageSize = mPlaylistRepository.count();
        playlistLiveData = mPlaylistRepository.getPlaylistLiveData(pageSize);
        mPlaylistMediator.addSource(playlistLiveData, mPlaylistMediator::setValue);
    }

    public MediatorLiveData<PagedList<Playlist>> getPlaylistLiveData() {
        return mPlaylistMediator;
    }

    public void search(String keyword) {
        mPlaylistMediator.removeSource(playlistLiveData);
        playlistLiveData = mPlaylistRepository.getPlaylistLiveData(keyword, pageSize);
        mPlaylistMediator.addSource(playlistLiveData, mPlaylistMediator::setValue);
    }

    public void insert(Playlist playlist) {
        mPlaylistRepository.insert(playlist);
    }

    public void delete(Playlist playlist) {
        mPlaylistRepository.delete(playlist);
    }
}
