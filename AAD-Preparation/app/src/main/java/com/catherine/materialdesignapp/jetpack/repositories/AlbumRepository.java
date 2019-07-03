package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumRepository {
    public final static String TAG = AlbumRepository.class.getSimpleName();
    private AlbumDao mAlbumDao;
    private final ExecutorService mIoExecutor;
    private static volatile AlbumRepository sInstance = null;

    // firebase
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    public static AlbumRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (AlbumRepository.class) {
                if (sInstance == null) {
                    AlbumRoomDatabase db = AlbumRoomDatabase.getDatabase(application);
                    sInstance = new AlbumRepository(db.albumDao(), Executors.newSingleThreadExecutor());

                    // update data source
                    sInstance.database = FirebaseDatabase.getInstance();
                    sInstance.myRef = sInstance.database.getReference(FirebaseDB.ALBUMS);
                    sInstance.myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0)
                                return;

                            // try to update data in a smarter way
                            Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                            sInstance.deleteAll();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Album album = child.getValue(Album.class);
                                Log.i(TAG, String.format("%s: %s", child.getKey(), album));
                                sInstance.insert(album);
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

    public AlbumRepository(AlbumDao dao, ExecutorService executor) {
        mAlbumDao = dao;
        mIoExecutor = executor;
    }

    public LiveData<List<Album>> getAlbumLiveData() {
        try {
            return mIoExecutor.submit(mAlbumDao::getAllAlbums).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Album album) {
        mIoExecutor.submit(() -> mAlbumDao.insert(album));
    }

    public void deleteAll() {
        mIoExecutor.submit(mAlbumDao::deleteAll);
    }

}