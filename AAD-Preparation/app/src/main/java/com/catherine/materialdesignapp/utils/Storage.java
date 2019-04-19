package com.catherine.materialdesignapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private final String STORAGE_KEY = "Main";
    private SharedPreferences sharedPreferences;

    public final static String NIGHT_MODE = "NIGHT_MODE";

    public Storage(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
    }

    public void save(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String retrieveString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void save(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int retrieveInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }
}
