package com.catherine.materialdesignapp.jetpack.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.catherine.materialdesignapp.jetpack.MapConverter;
import com.catherine.materialdesignapp.jetpack.daos.PlaylistDao;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;

/**
 * Created by catherine_chen on 2019-05-29.
 * Trend Micro
 * catherine_chen@trendmicro.com
 */
@Database(entities = {Playlist.class}, version = 1, exportSchema = false)
@TypeConverters(MapConverter.class)
public abstract class PlaylistRoomDatabase extends RoomDatabase {
    public PlaylistDao playlistDao;
    private static PlaylistRoomDatabase INSTANCE;

    public static PlaylistRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaylistRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlaylistRoomDatabase.class, "playlist_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
//                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
