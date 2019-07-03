package com.catherine.materialdesignapp.jetpack.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.repositories.PlaylistRepository;

import java.util.List;

public class PlaylistViewModel extends ViewModel {

    private PlaylistRepository mPlaylistRepository;
    private LiveData<List<Playlist>> playlistLiveData;

    public PlaylistViewModel(PlaylistRepository mPlaylistRepository) {
        this.mPlaylistRepository = mPlaylistRepository;
        playlistLiveData = mPlaylistRepository.getPlaylistLiveData();
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return playlistLiveData;
    }

    public void insert(Playlist playlist) {
        mPlaylistRepository.insert(playlist);
    }
}
