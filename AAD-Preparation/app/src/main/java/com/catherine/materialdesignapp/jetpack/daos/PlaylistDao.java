package com.catherine.materialdesignapp.jetpack.daos;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.catherine.materialdesignapp.jetpack.entities.Playlist;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Playlist playlist);

    @Query("DELETE FROM playlist_table")
    void deleteAll();

    @Query("DELETE FROM playlist_table WHERE name = :name")
    void delete(String name);

    @Query("SELECT * from playlist_table ORDER BY `index` ASC")
    LiveData<List<Playlist>> getAllPlaylist();

    @Query("SELECT * FROM playlist_table ORDER BY `index` ASC")
    DataSource.Factory<Integer, Playlist> getPlaylistDataSource();


    @Query("SELECT * FROM playlist_table WHERE name LIKE '%' || :keyword || '%'  ORDER BY `index` ASC")
    DataSource.Factory<Integer, Playlist> getPlaylistDataSource(String keyword);


    @Query("SELECT COUNT(*) FROM playlist_table")
    int count();
}
