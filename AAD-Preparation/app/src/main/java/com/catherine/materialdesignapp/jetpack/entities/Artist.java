package com.catherine.materialdesignapp.jetpack.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.catherine.materialdesignapp.listeners.ProguardIgnored;

@Entity(tableName = "artist_table")
public class Artist implements ProguardIgnored, Comparable<Artist> {
    private String image;

    @NonNull
    @PrimaryKey
    private String artist;

    private String url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{image = " + image + ", artist = " + artist + ", url = " + url + "}";
    }

    @Override
    public int compareTo(Artist o) {
        return artist.compareTo(o.getArtist());
    }
}