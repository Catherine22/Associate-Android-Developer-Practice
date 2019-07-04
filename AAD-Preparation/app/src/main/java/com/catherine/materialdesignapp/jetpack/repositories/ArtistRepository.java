package com.catherine.materialdesignapp.jetpack.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.catherine.materialdesignapp.FirebaseDB;
import com.catherine.materialdesignapp.jetpack.daos.ArtistDao;
import com.catherine.materialdesignapp.jetpack.databases.ArtistRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Artist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArtistRepository {
    public final static String TAG = ArtistRepository.class.getSimpleName();
    private ArtistDao mArtistDao;
    private final ExecutorService mIoExecutor;
    private static volatile ArtistRepository sInstance = null;

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    public static ArtistRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (ArtistRepository.class) {
                if (sInstance == null) {
                    ArtistRoomDatabase db = ArtistRoomDatabase.getDatabase(application);
                    sInstance = new ArtistRepository(db.artistDao(), Executors.newSingleThreadExecutor());

                    // update data source
                    sInstance.database = FirebaseDatabase.getInstance();
                    sInstance.myRef = sInstance.database.getReference(FirebaseDB.ARTISTS);
                    sInstance.myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0)
                                return;

                            // try to update data in a smarter way
                            Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                            sInstance.deleteAll();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Artist artist = child.getValue(Artist.class);
                                Log.i(TAG, String.format("%s: %s", child.getKey(), artist));
                                sInstance.insert(artist);
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

    public ArtistRepository(ArtistDao dao, ExecutorService executor) {
        mIoExecutor = executor;
        mArtistDao = dao;
    }

    public LiveData<List<Artist>> getArtistLiveData() {
        try {
            return mIoExecutor.submit(mArtistDao::getAllArtists).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    public LiveData<PagedList<Artist>> getArtistLiveData(int size) {
        try {
            return new LivePagedListBuilder<>(mIoExecutor.submit(() -> mArtistDao.getArtistDataSource()).get(), size).build();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<PagedList<Artist>> getArtistLiveData(String keyword, int size) {
        try {
            return new LivePagedListBuilder<>(mIoExecutor.submit(() -> mArtistDao.getArtistDataSource(keyword)).get(), size).build();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Artist artist) {
        mIoExecutor.submit(() -> mArtistDao.insert(artist));
    }

    public void deleteAll() {
        mIoExecutor.submit(mArtistDao::deleteAll);
    }

    public int count() {
        try {
            return mIoExecutor.submit(mArtistDao::count).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }
}