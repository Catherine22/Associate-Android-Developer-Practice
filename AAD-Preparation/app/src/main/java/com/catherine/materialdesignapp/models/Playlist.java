package com.catherine.materialdesignapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.catherine.materialdesignapp.listeners.ProguardIgnored;

import java.util.HashMap;
import java.util.Map;

public class Playlist implements Comparable<Playlist>, Cloneable, Parcelable, ProguardIgnored {

    private int index;

    private String name;

    private Map<String, Song> songs = new HashMap<>();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Map<String, Song> getSongs() {
        return songs;
    }

    public void setSongs(Map<String, Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    public Playlist() {

    }

    public Playlist(Parcel in) {
        index = in.readInt();
        name = in.readString();
        in.readMap(songs, Song.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(name);
        dest.writeMap(songs);
    }

    public Playlist clone() throws CloneNotSupportedException {
        Playlist clone = (Playlist) super.clone();
        clone.index = index;
        clone.name = name;
        clone.songs.putAll(songs);
        return clone;
    }

    @Override
    public int compareTo(Playlist o) {
        // return POSITIVE , if first object is greater than the second.
        // return 0 , if first object is equal to the second.
        // return NEGATIVE , if first object is less than the second one.
//        return this.name.compareTo(o.getName());
        return Integer.compare(this.index, o.getIndex());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Playlist playlist = (Playlist) o;
        return index == playlist.getIndex() &&
                name == playlist.getName() &&
                songs.equals(playlist.getSongs());
    }

    @Override
    public String toString() {
        return "{index = " + index + ", songs = " + songs + ", name = " + name + "}";
    }
}
