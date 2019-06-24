package com.catherine.materialdesignapp.jetpack.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.catherine.materialdesignapp.listeners.ProguardIgnored;

@Entity(tableName = "songs")
public class Song implements Cloneable, Parcelable, ProguardIgnored {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String artist;
    private String album;
    private String url;


    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

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
        id = in.readInt();
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
        dest.writeInt(id);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(url);
    }

    public Song clone() throws CloneNotSupportedException {
        Song clone = (Song) super.clone();
        clone.id = id;
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
        return id == song.getID() &&
                artist == song.getArtist() &&
                album == song.getAlbum() &&
                url == song.getUrl();
    }

    @Override
    public String toString() {
        return "{id = " + id + ", artist = " + artist + ", album = " + album + ", url = " + url + "}";
    }
}