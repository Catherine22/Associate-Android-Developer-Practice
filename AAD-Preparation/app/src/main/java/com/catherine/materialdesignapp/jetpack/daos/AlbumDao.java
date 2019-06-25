package com.catherine.materialdesignapp.jetpack.daos;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.*;
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

    @Query("SELECT * FROM album_table ORDER BY _id ASC")
    LiveData<List<Album>> getAllAlbums();

    @RawQuery
    Cursor select(SupportSQLiteQuery query);

    @Query("SELECT COUNT(*) FROM album_table")
    int count();

}
