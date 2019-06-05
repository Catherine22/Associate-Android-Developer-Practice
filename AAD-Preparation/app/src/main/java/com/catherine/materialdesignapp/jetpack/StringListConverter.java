package com.catherine.materialdesignapp.jetpack;

import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class StringListConverter {
    @TypeConverter
    public static List<String> fromString(String value) {
        try {
            JSONArray ja = new JSONArray(value);
            List<String> songList = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                songList.add(ja.getString(i));
            }
            return songList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String fromStringList(List<String> value) {
        if (value == null || value.size() == 0) {
            return new JSONArray().toString();
        }
        JSONArray ja = new JSONArray();
        for (int i = 0; i < value.size(); i++) {
            ja.put(value.get(i));
        }
        return ja.toString();
    }
}
