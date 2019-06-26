package com.catherine.materialdesignapp.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.catherine.materialdesignapp.jetpack.StringListConverter;
import com.catherine.materialdesignapp.jetpack.daos.AlbumDao;
import com.catherine.materialdesignapp.jetpack.databases.AlbumRoomDatabase;
import com.catherine.materialdesignapp.jetpack.entities.Album;

import java.util.Arrays;

public class AlbumsProvider extends ContentProvider {
    public final static String TAG = AlbumsProvider.class.getSimpleName();
    private final static String AUTHORITY = "com.catherine.materialdesignapp.providers.AlbumsProvider";
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    private static final int ALBUMS = 1;
    private static final int ALBUMS_ID = 2;

    static {
        sURIMatcher.addURI(AUTHORITY, "albums", ALBUMS);
        sURIMatcher.addURI(AUTHORITY, "albums/#", ALBUMS_ID);
    }

    private AlbumDao albumDao;


    @Override
    public boolean onCreate() {
        if (getContext() == null)
            return false;

        AlbumRoomDatabase albumRoomDatabase = Room.databaseBuilder(getContext(),
                AlbumRoomDatabase.class, "album_database")
                .fallbackToDestructiveMigration()
                .build();
        albumDao = albumRoomDatabase.albumDao();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SupportSQLiteQuery mQuery;
        switch (sURIMatcher.match(uri)) {
            case ALBUMS:
                mQuery = SupportSQLiteQueryBuilder
                        .builder("album_table")
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder)
                        .create();
                return albumDao.select(mQuery);
            case ALBUMS_ID:
                if (!TextUtils.isEmpty(selection) && !selection.trim().contains("_id=")) {
                    String mSelection = selection + " AND _id = ?";
                    String[] mSelectionArgs = new String[selectionArgs.length + 1];
                    System.arraycopy(selectionArgs, 0, mSelectionArgs, 0, selectionArgs.length);
                    mSelectionArgs[mSelectionArgs.length - 1] = ContentUris.parseId(uri) + "";
                    mQuery = SupportSQLiteQueryBuilder
                            .builder("album_table")
                            .columns(projection)
                            .selection(mSelection, mSelectionArgs)
                            .orderBy(sortOrder)
                            .create();
                } else {
                    mQuery = SupportSQLiteQueryBuilder
                            .builder("album_table")
                            .columns(projection)
                            .selection(selection, selectionArgs)
                            .orderBy(sortOrder)
                            .create();
                }
                return albumDao.select(mQuery);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public String getType(@NonNull Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case ALBUMS:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + "albums";
            case ALBUMS_ID:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + "albums/#";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (getContext() == null || values == null)
            return null;

        switch (sURIMatcher.match(uri)) {
            case ALBUMS:
                Album album = new Album();
                if (values.containsKey("title"))
                    album.setTitle(values.getAsString("title"));

                if (values.containsKey("artist"))
                    album.setArtist(values.getAsString("artist"));

                if (values.containsKey("image"))
                    album.setImage(values.getAsString("image"));

                if (values.containsKey("thumbnail_image"))
                    album.setThumbnail_image(values.getAsString("thumbnail_image"));

                if (values.containsKey("url"))
                    album.setUrl(values.getAsString("url"));

                if (values.containsKey("songs")) {
                    album.setSongs(StringListConverter.fromString(values.getAsString("songs")));
                }
                long id = albumDao.insert(album);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case ALBUMS_ID:
                throw new IllegalArgumentException("Invalid URI, cannot insert with _id: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        SimpleSQLiteQuery query;
        switch (sURIMatcher.match(uri)) {
            case ALBUMS:
                if (TextUtils.isEmpty(selection) || selection.trim().contains("_id="))
                    throw new IllegalArgumentException("Invalid URI, cannot delete without _id: " + uri);

                query = new SimpleSQLiteQuery("DELETE FROM album_table WHERE " + selection, selectionArgs);
                count = albumDao.deleteByRawQuery(query);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            case ALBUMS_ID:
                if (getContext() == null)
                    return -1;

                String mSelection;
                String[] mSelectionArgs;
                if (TextUtils.isEmpty(selection)) {
                    mSelection = "DELETE FROM album_table WHERE _id = ?";
                    mSelectionArgs = new String[]{ContentUris.parseId(uri) + ""};
                } else if (!selection.trim().contains("_id=")) {
                    mSelection = "DELETE FROM album_table WHERE " + selection + " AND _id = ?";
                    mSelectionArgs = new String[selectionArgs.length + 1];
                    System.arraycopy(selectionArgs, 0, mSelectionArgs, 0, selectionArgs.length);
                    mSelectionArgs[mSelectionArgs.length - 1] = ContentUris.parseId(uri) + "";
                } else {
                    throw new IllegalArgumentException("Invalid URI, cannot delete with duplicated _id: " + uri);
                }

                query = new SimpleSQLiteQuery(mSelection, mSelectionArgs);
                count = albumDao.deleteByRawQuery(query);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        if (getContext() == null)
            return -1;

        if (sURIMatcher.match(uri) != ALBUMS && sURIMatcher.match(uri) != ALBUMS_ID)
            throw new IllegalArgumentException("Invalid URI: " + uri);

        if (values == null)
            throw new IllegalArgumentException("Invalid URI, cannot update without values: " + uri);

        String header = "UPDATE album_table SET ";
        StringBuilder mQuery = new StringBuilder(header);
        if (values.containsKey("title"))
            mQuery.append(appendQuery(values, "title"));

        if (values.containsKey("artist"))
            mQuery.append(appendQuery(values, "artist"));

        if (values.containsKey("image"))
            mQuery.append(appendQuery(values, "image"));

        if (values.containsKey("thumbnail_image"))
            mQuery.append(appendQuery(values, "thumbnail_image"));

        if (values.containsKey("url"))
            mQuery.append(appendQuery(values, "url"));

        if (values.containsKey("songs"))
            mQuery.append(appendQuery(values, "songs"));

        if (header.length() == mQuery.length())
            throw new IllegalArgumentException("Invalid URI, cannot update with incorrect values: " + uri);

        mQuery.delete(mQuery.length() - ", ".length(), mQuery.length());

        String[] mSelectionArgs = selectionArgs;
        if (sURIMatcher.match(uri) == ALBUMS) {
            if (!TextUtils.isEmpty(selection)) {
                mQuery.append(" WHERE ");
                mQuery.append(selection);
            }
        } else {
            // sURIMatcher.match(uri) == ALBUMS_ID
            if (TextUtils.isEmpty(selection)) {
                mQuery.append(" WHERE _id = ?");
                mSelectionArgs = new String[]{ContentUris.parseId(uri) + ""};
            } else if (!selection.trim().contains("_id=")) {
                mQuery.append(" WHERE ");
                mQuery.append(selection);
                mSelectionArgs = new String[selectionArgs.length + 1];
                System.arraycopy(selectionArgs, 0, mSelectionArgs, 0, selectionArgs.length);
                mSelectionArgs[mSelectionArgs.length - 1] = ContentUris.parseId(uri) + "";
            } else {
                throw new IllegalArgumentException("Invalid URI, cannot update with duplicated _id: " + uri);
            }
        }

        Log.d(TAG, "query: " + mQuery.toString());
        Log.d(TAG, "args: " + Arrays.toString(mSelectionArgs));
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(mQuery.toString(), mSelectionArgs);
        Cursor cursor = albumDao.updateByRawQuery(query);
        getContext().getContentResolver().notifyChange(uri, null);
        return cursor.getCount();
    }

    private String appendQuery(ContentValues values, String str) {
        StringBuilder sb = new StringBuilder();
        if (values.containsKey(str)) {
            sb.append(str);
            sb.append(" = ");
            sb.append(values.getAsString(str));
            sb.append(", ");
        }
        return sb.toString();
    }
}
