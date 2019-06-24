package com.catherine.materialdesignapp.jetpack;

import androidx.room.TypeConverter;
import com.catherine.materialdesignapp.jetpack.entities.Song;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SongMapConverter {
    @TypeConverter
    public static Map<String, Song> fromString(String value) {
        try {
            JSONObject jo = new JSONObject(value);
            Iterator<String> keys = jo.keys();
            Map<String, Song> map = new HashMap<>();
            while (keys.hasNext()) {
                String k = keys.next();
                JSONObject j = jo.getJSONObject(k);
                Song s = new Song();
                s.setAlbum(j.optString("album"));
                s.setArtist(j.optString("artist"));
                s.setID(j.optInt("id"));
                s.setUrl(j.optString("url"));
                map.put(k, s);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String fromStringMap(Map<String, Song> value) {
        if (value == null || value.size() == 0) {
            return new JSONObject().toString();
        }
        try {
            Iterator<String> keys = value.keySet().iterator();
            JSONObject jo = new JSONObject(value);
            while (keys.hasNext()) {
                String k = keys.next();
                Song s = value.get(k);
                JSONObject j = new JSONObject();
                j.put("album", s.getAlbum());
                j.put("artist", s.getArtist());
                j.put("id", s.getID());
                j.put("url", s.getUrl());
                jo.put(k, j);
            }
            return jo.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
