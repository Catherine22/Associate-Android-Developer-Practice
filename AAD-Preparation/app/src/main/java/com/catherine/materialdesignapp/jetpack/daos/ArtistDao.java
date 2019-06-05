package com.catherine.materialdesignapp.jetpack.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.catherine.materialdesignapp.jetpack.entities.Artist;

import java.util.List;

@Dao
public interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Artist artist);

    @Query("DELETE FROM artist_table")
    void deleteAll();

    @Query("SELECT * from artist_table ORDER BY `artist` ASC")
    LiveData<List<Artist>> getAllArtists();
}
