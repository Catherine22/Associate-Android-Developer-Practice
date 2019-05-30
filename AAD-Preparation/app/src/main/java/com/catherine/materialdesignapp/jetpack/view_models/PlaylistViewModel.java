package com.catherine.materialdesignapp.jetpack.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.repositories.PlaylistRepositry;

import java.util.List;

/**
 * Created by catherine_chen on 2019-05-29.
 * Trend Micro
 * catherine_chen@trendmicro.com
 */
public class PlaylistViewModel extends AndroidViewModel {

    private PlaylistRepositry mPlaylistRepositry;
    private LiveData<List<Playlist>> mAllPlaylists;


    public PlaylistViewModel(Application application) {
        super(application);
        mPlaylistRepositry = new PlaylistRepositry(application);
        mAllPlaylists = mPlaylistRepositry.getAllPlaylists();
    }

    LiveData<List<Playlist>> getAllPlaylists() {
        return mAllPlaylists;
    }

    public void insert(Playlist playlist) {
        mPlaylistRepositry.insert(playlist);
    }

}
