package com.catherine.materialdesignapp.jetpack.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.catherine.materialdesignapp.Constants;
import com.catherine.materialdesignapp.jetpack.StringListConverter;
import com.catherine.materialdesignapp.jetpack.daos.AlbumDao;
import com.catherine.materialdesignapp.jetpack.entities.Album;

@Database(entities = {Album.class}, version = Constants.ROOM_DATABASE_VERSION, exportSchema = false)
@TypeConverters(StringListConverter.class)
public abstract class AlbumRoomDatabase extends RoomDatabase {
    public abstract AlbumDao albumDao();

    private static AlbumRoomDatabase INSTANCE;

    public static AlbumRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AlbumRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AlbumRoomDatabase.class, "album_database")
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
