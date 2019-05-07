package com.catherine.materialdesignapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.catherine.materialdesignapp.listeners.ProguardIgnored;

public class Song implements Cloneable, Parcelable, ProguardIgnored {
    private String artist;
    private String album;
    private String url;


    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public Song() {

    }

    public Song(Parcel in) {
        artist = in.readString();
        album = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(url);
    }

    public Song clone() throws CloneNotSupportedException {
        Song clone = (Song) super.clone();
        clone.artist = artist;
        clone.album = album;
        clone.url = url;
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Song song = (Song) o;
        return artist == song.getArtist() &&
                album == song.getAlbum() &&
                url == song.getUrl();
    }

    @Override
    public String toString() {
        return "{artist = " + artist + ", album = " + album + ", url = " + url + "}";
    }
}