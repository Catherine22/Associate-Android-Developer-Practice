package com.catherine.materialdesignapp.jetpack.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.repositories.PlaylistRepository;

import java.util.List;

/**
 * Created by catherine_chen on 2019-05-29.
 * Trend Micro
 * catherine_chen@trendmicro.com
 */
public class PlaylistViewModel extends AndroidViewModel {

    private PlaylistRepository mPlaylistRepository;
    private LiveData<List<Playlist>> playlistLiveData;


    public PlaylistViewModel(Application application) {
        super(application);
        mPlaylistRepository = new PlaylistRepository(application);
        playlistLiveData = mPlaylistRepository.getAllPlaylists();
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return playlistLiveData;
    }

    public void insert(Playlist playlist) {
        mPlaylistRepository.insert(playlist);
    }

}
