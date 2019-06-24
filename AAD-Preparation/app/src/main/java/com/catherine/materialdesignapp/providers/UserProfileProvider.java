package com.catherine.materialdesignapp.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.catherine.materialdesignapp.MyApplication;

public class UserProfileProvider extends ContentProvider {
    public final static String TAG = UserProfileProvider.class.getSimpleName();
    private final static String AUTHORITY = MyApplication.INSTANCE.getPackageName();
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private final static int PROFILE = 1;
    private final static int PROFILE_ID = 2;
    private final static int PROFILE_FILTER = 3;

    static {
        sURIMatcher.addURI(AUTHORITY, "profile", PROFILE);
        sURIMatcher.addURI(AUTHORITY, "profile/#", PROFILE_ID);
        sURIMatcher.addURI(AUTHORITY, "profile/filter/*", PROFILE_FILTER);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
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
