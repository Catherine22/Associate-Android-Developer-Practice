package com.catherine.materialdesignapp.jetpack.view_models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.repositories.PlaylistRepository;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private PlaylistRepository mPlaylistRepository;
    private LiveData<List<Playlist>> playlistLiveData;


    public PlaylistViewModel(Application application) {
        super(application);
        mPlaylistRepository = new PlaylistRepository(application);
        playlistLiveData = mPlaylistRepository.getPlaylistLiveData();
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return playlistLiveData;
    }

    public void insert(Playlist playlist) {
        mPlaylistRepository.insert(playlist);
    }

}
