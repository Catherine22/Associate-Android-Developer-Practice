package com.catherine.materialdesignapp.models;

import com.catherine.materialdesignapp.listeners.ProguardIgnored;

import java.util.Map;

public class Playlist implements Comparable<Playlist>, Cloneable, ProguardIgnored {

    private int index;

    private Map<String, Song> songs;

    private String name;

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
