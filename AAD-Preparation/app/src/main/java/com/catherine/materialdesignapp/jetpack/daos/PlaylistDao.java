package com.catherine.materialdesignapp.jetpack.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.catherine.materialdesignapp.jetpack.entities.Playlist;

import java.util.List;

/**
 * Created by catherine_chen on 2019-05-29.
 * Trend Micro
 * catherine_chen@trendmicro.com
 */

@Dao
public interface PlaylistDao {

    @Insert
    void insert(Playlist playlist);

    @Query("DELETE FROM playlist_table")
    void deleteAll();

    @Query("SELECT * from playlist_table ORDER BY `index` ASC")
    LiveData<List<Playlist>> getAllPlaylists();
}
