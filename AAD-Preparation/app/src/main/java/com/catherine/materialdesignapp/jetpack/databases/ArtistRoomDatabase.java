package com.catherine.materialdesignapp.jetpack.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.catherine.materialdesignapp.Constants;
import com.catherine.materialdesignapp.jetpack.daos.ArtistDao;
import com.catherine.materialdesignapp.jetpack.entities.Artist;

@Database(entities = {Artist.class}, version = Constants.ROOM_DATABASE_VERSION, exportSchema = false)
public abstract class ArtistRoomDatabase extends RoomDatabase {
    public abstract ArtistDao artistDao();

    private static ArtistRoomDatabase INSTANCE;

    public static ArtistRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArtistRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArtistRoomDatabase.class, "artist_database")
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
