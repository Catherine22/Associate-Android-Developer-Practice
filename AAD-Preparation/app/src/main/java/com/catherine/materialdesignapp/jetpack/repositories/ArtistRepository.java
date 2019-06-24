package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.catherine.materialdesignapp.jetpack.daos.ArtistDao;
import com.catherine.materialdesignapp.jetpack.databases.ArtistRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Artist;

import java.util.List;

public class ArtistRepository {
    private ArtistDao mArtistDao;
    private LiveData<List<Artist>> artistLiveData;

    public ArtistRepository(Application application) {
        ArtistRoomDatabase db = ArtistRoomDatabase.getDatabase(application);
        mArtistDao = db.artistDao();
        artistLiveData = mArtistDao.getAllArtists();
    }

    public LiveData<List<Artist>> getArtistLiveData() {
        return artistLiveData;
    }

    public void insert(Artist artist) {
        new insertAsyncTask(mArtistDao).execute(artist);
    }

    private static class insertAsyncTask extends AsyncTask<Artist, Void, Void> {

        private ArtistDao mAsyncTaskDao;

        insertAsyncTask(ArtistDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Artist... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}