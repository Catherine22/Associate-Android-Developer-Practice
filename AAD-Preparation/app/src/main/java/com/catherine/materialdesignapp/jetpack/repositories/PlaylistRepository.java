package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.jetpack.daos.PlaylistDao;
import com.catherine.materialdesignapp.jetpack.databases.PlaylistRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;

import java.util.List;

/**
 * Created by catherine_chen on 2019-05-29.
 * Trend Micro
 * catherine_chen@trendmicro.com
 */
public class PlaylistRepository {
    private PlaylistDao mPlaylistDao;
    private LiveData<List<Playlist>> playlistLiveData;

    public PlaylistRepository(Application application) {
        PlaylistRoomDatabase db = PlaylistRoomDatabase.getDatabase(application);
        mPlaylistDao = db.playlistDao();
        playlistLiveData = mPlaylistDao.getAllPlaylists();
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return playlistLiveData;
    }

    public void insert(Playlist playlist) {
        new insertAsyncTask(mPlaylistDao).execute(playlist);
    }

    private static class insertAsyncTask extends AsyncTask<Playlist, Void, Void> {

        private PlaylistDao mAsyncTaskDao;

        insertAsyncTask(PlaylistDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Playlist... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
