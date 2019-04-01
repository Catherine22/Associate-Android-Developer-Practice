package com.catherine.materialdesignapp.content_providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.util.Log;

import com.catherine.materialdesignapp.MyApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CallLogDao extends Dao<String> {
    public final static String TAG = CallLogDao.class.getSimpleName();
    private final Uri database = Uri.parse("content://call_log/calls");
    private ContentResolver contentResolver;

    public CallLogDao() {
        contentResolver = MyApplication.INSTANCE.getApplicationContext().getContentResolver();
    }


    public List<String> read() {
        String[] projection = {CallLog.Calls.NUMBER};
        Cursor cursor = contentResolver.query(database, projection, null, null, null);
        List<String> words = new LinkedList<>();
        if (cursor != null) {
            Log.d(TAG, "cursor counts: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                words.add(number);
                Log.d(TAG, "Log: " + number);
            }
            cursor.close();
        }
        return words;
    }

    @Override
    void insert(String newValue) {
        ContentValues newValues = new ContentValues();
        newValues.put(CallLog.Calls.NUMBER, newValue);
        contentResolver.registerContentObserver(database, true, new CallLogObserver(new Handler()));
        Uri newUri = contentResolver.insert(database, newValues);
        Log.d(TAG, String.format(Locale.US, "new uri: %s", newUri.getPath()));
    }

    @Override
    void update(String oldValue, String newValue) {

    }

    @Override
    void delete(String value) {

    }

    private class CallLogObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        CallLogObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, String.format(Locale.US, "onChange: %b", selfChange));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(TAG, String.format(Locale.US, "onChange: %b, %s", selfChange, uri.getPath()));
            contentResolver.unregisterContentObserver(this);
        }
    }


}
