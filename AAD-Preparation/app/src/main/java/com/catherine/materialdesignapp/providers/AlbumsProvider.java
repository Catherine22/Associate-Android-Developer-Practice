package com.catherine.materialdesignapp.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import com.catherine.materialdesignapp.jetpack.databases.AlbumRoomDatabase;

public class AlbumsProvider extends ContentProvider {
    public final static String TAG = AlbumsProvider.class.getSimpleName();
    private final static String AUTHORITY = "com.catherine.materialdesignapp.providers.AlbumsProvider";
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sURIMatcher.addURI(AUTHORITY, "albums", 0);
        sURIMatcher.addURI(AUTHORITY, "albums/#", 1);
    }

    private AlbumRoomDatabase albumRoomDatabase;


    @Override
    public boolean onCreate() {
        albumRoomDatabase = Room.databaseBuilder(getContext(),
                AlbumRoomDatabase.class, "album_database")
                // Wipes and rebuilds instead of migrating
                // if no Migration object.
                // Migration is not part of this practical.
                .fallbackToDestructiveMigration()
                .build();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String query = "SELECT title FROM album_table";
        String[] args = null;
        if (!TextUtils.isEmpty(sortOrder)) {
            query += " ORDER BY ? ASC";
            args = new String[]{sortOrder};
        }
        return albumRoomDatabase.query(query, args);
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
