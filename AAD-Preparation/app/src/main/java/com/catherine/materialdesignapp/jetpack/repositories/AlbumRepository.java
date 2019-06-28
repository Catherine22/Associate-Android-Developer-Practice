package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.FirebaseDB;
import com.catherine.materialdesignapp.jetpack.daos.AlbumDao;
import com.catherine.materialdesignapp.jetpack.databases.AlbumRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Album;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AlbumRepository {
    public final static String TAG = AlbumRepository.class.getSimpleName();
    private AlbumDao mAlbumDao;
    private LiveData<List<Album>> albumLiveData;

    // firebase
    private DatabaseReference myRef;
    private ValueEventListener firebaseValueEventListener;

    public AlbumRepository(Application application) {
        AlbumRoomDatabase db = AlbumRoomDatabase.getDatabase(application);
        mAlbumDao = db.albumDao();
        albumLiveData = mAlbumDao.getAllAlbums();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(FirebaseDB.ALBUMS);
        firebaseValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0)
                    return;
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                new updateAllAsyncTask(mAlbumDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
    }

    public LiveData<List<Album>> getAlbumLiveData() {
        return albumLiveData;
    }

    public void insert(Album album) {
        new insertAsyncTask(mAlbumDao).execute(album);
    }

    public void releaseFirebase() {
        myRef.removeEventListener(firebaseValueEventListener);
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

    private static class updateAllAsyncTask extends AsyncTask<Iterable<DataSnapshot>, Void, Void> {

        private AlbumDao mAsyncTaskDao;

        updateAllAsyncTask(AlbumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Iterable<DataSnapshot>... params) {
            mAsyncTaskDao.deleteAll();
            for (DataSnapshot child : params[0]) {
                Album album = child.getValue(Album.class);
                Log.i(TAG, String.format("%s: %s", child.getKey(), album));
                mAsyncTaskDao.insert(album);
            }
            return null;
        }
    }

}