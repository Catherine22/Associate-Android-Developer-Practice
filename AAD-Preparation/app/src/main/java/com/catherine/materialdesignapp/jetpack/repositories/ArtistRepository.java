package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.jetpack.daos.ArtistDao;
import com.catherine.materialdesignapp.jetpack.databases.ArtistRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Artist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ArtistRepository {
    public final static String TAG = ArtistRepository.class.getSimpleName();
    private ArtistDao mArtistDao;
    private LiveData<List<Artist>> artistLiveData;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener firebaseValueEventListener;

    public ArtistRepository(Application application) {
        ArtistRoomDatabase db = ArtistRoomDatabase.getDatabase(application);
        mArtistDao = db.artistDao();
        artistLiveData = mArtistDao.getAllArtists();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("artists");
        firebaseValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0)
                    return;
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                new updateAllAsyncTask(mArtistDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
    }

    public LiveData<List<Artist>> getArtistLiveData() {
        return artistLiveData;
    }

    public void insert(Artist artist) {
        new insertAsyncTask(mArtistDao).execute(artist);
    }

    public void releaseFirebase() {
        myRef.removeEventListener(firebaseValueEventListener);
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

    private static class updateAllAsyncTask extends AsyncTask<Iterable<DataSnapshot>, Void, Void> {

        private ArtistDao mAsyncTaskDao;

        updateAllAsyncTask(ArtistDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Iterable<DataSnapshot>... params) {
            mAsyncTaskDao.deleteAll();
            for (DataSnapshot child : params[0]) {
                Artist artist = child.getValue(Artist.class);
                Log.i(TAG, String.format("%s: %s", child.getKey(), artist));
                mAsyncTaskDao.insert(artist);
            }
            return null;
        }
    }

}