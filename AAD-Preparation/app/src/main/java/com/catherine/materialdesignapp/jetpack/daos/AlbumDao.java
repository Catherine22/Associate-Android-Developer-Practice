package com.catherine.materialdesignapp.jetpack.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.catherine.materialdesignapp.jetpack.entities.Album;

import java.util.List;

@Dao
public interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Album album);

    @Query("DELETE FROM album_table")
    void deleteAll();

    @Query("SELECT * from album_table ORDER BY `title` ASC")
    LiveData<List<Album>> getAllAlbums();
}
