package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.jetpack.daos.AlbumDao;
import com.catherine.materialdesignapp.jetpack.databases.AlbumRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Album;

import java.util.List;

public class AlbumRepository {
    private AlbumDao mAlbumDao;
    private LiveData<List<Album>> albumLiveData;

    public AlbumRepository(Application application) {
        AlbumRoomDatabase db = AlbumRoomDatabase.getDatabase(application);
        mAlbumDao = db.albumDao();
        albumLiveData = mAlbumDao.getAllAlbums();
    }

    public LiveData<List<Album>> getAlbumLiveData() {
        return albumLiveData;
    }

    public void insert(Album album) {
        new insertAsyncTask(mAlbumDao).execute(album);
    }

    private static class insertAsyncTask extends AsyncTask<Album, Void, Void> {

        private AlbumDao mAsyncTaskDao;

        insertAsyncTask(AlbumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Album... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}