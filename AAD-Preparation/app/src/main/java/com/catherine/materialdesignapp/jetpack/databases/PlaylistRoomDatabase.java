package com.catherine.materialdesignapp.jetpack.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.catherine.materialdesignapp.Constants;
import com.catherine.materialdesignapp.jetpack.SongMapConverter;
import com.catherine.materialdesignapp.jetpack.daos.PlaylistDao;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;

@Database(entities = {Playlist.class}, version = Constants.ROOM_DATABASE_VERSION, exportSchema = false)
@TypeConverters(SongMapConverter.class)
public abstract class PlaylistRoomDatabase extends RoomDatabase {

    public abstract PlaylistDao playlistDao();

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
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
