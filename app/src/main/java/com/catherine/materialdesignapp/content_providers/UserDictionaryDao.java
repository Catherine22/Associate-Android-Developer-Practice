package com.catherine.materialdesignapp.content_providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.UserDictionary;
import android.util.Log;

import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.models.Word;

import java.util.LinkedList;
import java.util.List;

public class UserDictionaryDao {
    public final static String TAG = UserDictionaryDao.class.getSimpleName();
    private final Uri database = Uri.parse("content://user_dictionary/words");
    private ContentResolver contentResolver;

    public UserDictionaryDao() {
        contentResolver = MyApplication.INSTANCE.getApplicationContext().getContentResolver();
//        contentResolver.registerContentObserver(database, true, new UserDictionaryObserver(new Handler()));
    }

    public List<Word> query() {
        String[] projection = {UserDictionary.Words.WORD, UserDictionary.Words.FREQUENCY, UserDictionary.Words.SHORTCUT, UserDictionary.Words.LOCALE};
        Cursor cursor = contentResolver.query(database, projection, null, null, null);
        List<Word> words = new LinkedList<>();
        if (cursor != null) {
            Log.d(TAG, "cursor counts: " + cursor.getCount());
            while (cursor.moveToNext()) {
                Word word = new Word();
                word.word = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.WORD));
                word.frequency = cursor.getInt(cursor.getColumnIndex(UserDictionary.Words.FREQUENCY));
                word.shortcut = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.SHORTCUT));
                word.locale = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.SHORTCUT));
                words.add(word);
            }
            cursor.close();
        }
        return words;
    }

    public void insert(Word word) {
        ContentValues newValues = new ContentValues();
        newValues.put(UserDictionary.Words.SHORTCUT, word.locale);
        newValues.put(UserDictionary.Words.LOCALE, word.locale);
        newValues.put(UserDictionary.Words.WORD, word.word);
        newValues.put(UserDictionary.Words.FREQUENCY, word.frequency);
        contentResolver.registerContentObserver(database, true, new UserDictionaryObserver(new Handler()));
        contentResolver.insert(database, newValues);
    }

    private class UserDictionaryObserver extends ContentObserver {
        UserDictionaryObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(TAG, "onChange:" + selfChange + "/uri:" + uri);
            contentResolver.unregisterContentObserver(this);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, "onChange:" + selfChange);
            contentResolver.unregisterContentObserver(this);
        }
    }
}
