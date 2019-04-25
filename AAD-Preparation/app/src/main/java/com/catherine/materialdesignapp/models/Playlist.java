package com.catherine.materialdesignapp.models;

public class Playlist {
    private Song[] songs;

    private String name;

    public Song[] getSongs() {
        return songs;
    }

    public void setSongs(Song[] songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassPojo [songs = " + songs + ", name = " + name + "]";
    }
}
