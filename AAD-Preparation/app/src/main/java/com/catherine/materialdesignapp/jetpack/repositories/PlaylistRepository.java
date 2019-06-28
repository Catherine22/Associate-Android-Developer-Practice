package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.catherine.materialdesignapp.FirebaseDB;
import com.catherine.materialdesignapp.jetpack.daos.PlaylistDao;
import com.catherine.materialdesignapp.jetpack.databases.PlaylistRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PlaylistRepository {
    public final static String TAG = PlaylistRepository.class.getSimpleName();

    private PlaylistDao mPlaylistDao;
    private LiveData<List<Playlist>> playlistLiveData;

    // firebase
    private DatabaseReference myRef;
    private ValueEventListener firebaseValueEventListener;

    public PlaylistRepository(Application application) {
        PlaylistRoomDatabase db = PlaylistRoomDatabase.getDatabase(application);
        mPlaylistDao = db.playlistDao();
        playlistLiveData = mPlaylistDao.getAllPlaylists();

        // initialise firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String DB_PATH = FirebaseDB.PLAYLIST;
        myRef = database.getReference(DB_PATH);

        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        // Failed to read value
        firebaseValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0)
                    return;
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                new updateAllAsyncTask(mPlaylistDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
    }

    public LiveData<List<Playlist>> getPlaylistLiveData() {
        return playlistLiveData;
    }

    public void insert(Playlist playlist) {
        new insertAsyncTask(mPlaylistDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, playlist);
    }

    public void releaseFirebase() {
        myRef.removeEventListener(firebaseValueEventListener);
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

    private static class updateAllAsyncTask extends AsyncTask<Iterable<DataSnapshot>, Void, Void> {

        private PlaylistDao mAsyncTaskDao;

        updateAllAsyncTask(PlaylistDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Iterable<DataSnapshot>... params) {
            mAsyncTaskDao.deleteAll();
            for (DataSnapshot child : params[0]) {
                Playlist playlist = child.getValue(Playlist.class);
                Log.i(TAG, String.format("%s: %s", child.getKey(), playlist));
                mAsyncTaskDao.insert(playlist);
            }
            return null;
        }
    }

}
