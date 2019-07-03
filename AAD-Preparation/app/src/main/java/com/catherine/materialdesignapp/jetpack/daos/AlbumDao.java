package com.catherine.materialdesignapp.jetpack.daos;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.catherine.materialdesignapp.jetpack.entities.Album;

import java.util.List;

@Dao
public interface AlbumDao {
    /**
     * @param album
     * @return _id (primary key)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Album album);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Album[] albums);

    /**
     * @return row ID
     */
    @Query("DELETE FROM album_table")
    int deleteAll();

    /**
     * @return row ID
     */
    @RawQuery
    int deleteByRawQuery(SupportSQLiteQuery query);

    /**
     * @param album
     * @return row ID
     */
    @Update
    int updateAll(Album[] album);

    /**
     * update
     *
     * @param query
     * @return
     */
    @RawQuery
    Cursor updateByRawQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM album_table ORDER BY title ASC")
    LiveData<List<Album>> getAllAlbums();

    @Query("SELECT * FROM album_table ORDER BY title ASC")
    DataSource.Factory<Integer, Album> getAlbumDataSource();


    @Query("SELECT * FROM album_table WHERE title LIKE '%' || :keyword || '%' OR artist LIKE '%' || :keyword || '%' ORDER BY title ASC")
    DataSource.Factory<Integer, Album> getAlbumDataSource(String keyword);

    @RawQuery
    Cursor select(SupportSQLiteQuery query);

    @Query("SELECT COUNT(*) FROM album_table")
    int count();

}
