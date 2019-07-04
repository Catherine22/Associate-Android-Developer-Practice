package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistRepository {
    public final static String TAG = PlaylistRepository.class.getSimpleName();

    private PlaylistDao mPlaylistDao;
    private final ExecutorService mIoExecutor;
    private static volatile PlaylistRepository sInstance = null;

    // firebase
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public static PlaylistRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (PlaylistRepository.class) {
                if (sInstance == null) {
                    PlaylistRoomDatabase db = PlaylistRoomDatabase.getDatabase(application);
                    sInstance = new PlaylistRepository(db.playlistDao(), Executors.newSingleThreadExecutor());

                    // update data source
                    sInstance.database = FirebaseDatabase.getInstance();
                    sInstance.myRef = sInstance.database.getReference(FirebaseDB.PLAYLIST);
                    sInstance.myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0)
                                return;

                            // try to update data in a smarter way
                            Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                            sInstance.deleteAll();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Playlist playlist = child.getValue(Playlist.class);
                                Log.i(TAG, String.format("%s: %s", child.getKey(), playlist));
                                sInstance.insert(playlist);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
            }
        }
        return sInstance;
    }

    public PlaylistRepository(PlaylistDao dao, ExecutorService executor) {
        mPlaylistDao = dao;
        mIoExecutor = executor;
    }

    public LiveData<List<Playlist>> getPlaylistLiveData() {
        try {
            return mIoExecutor.submit(mPlaylistDao::getAllPlaylist).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<PagedList<Playlist>> getPlaylistLiveData(int size) {
        try {
            return new LivePagedListBuilder<>(mIoExecutor.submit(() -> mPlaylistDao.getPlaylistDataSource()).get(), size).build();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<PagedList<Playlist>> getPlaylistLiveData(String keyword, int size) {
        try {
            return new LivePagedListBuilder<>(mIoExecutor.submit(() -> mPlaylistDao.getPlaylistDataSource(keyword)).get(), size).build();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteAll() {
        mIoExecutor.submit(mPlaylistDao::deleteAll);
    }

    public void delete(Playlist playlist) {
        mIoExecutor.submit(() -> mPlaylistDao.delete(playlist.getName()));
    }

    public void insert(Playlist playlist) {
        mIoExecutor.submit(() -> mPlaylistDao.insert(playlist));
    }

    public int count() {
        try {
            return mIoExecutor.submit(mPlaylistDao::count).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
