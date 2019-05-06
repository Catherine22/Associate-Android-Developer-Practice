package com.catherine.materialdesignapp.models;

import com.catherine.materialdesignapp.listeners.ProguardIgnored;

public class Song implements Cloneable, ProguardIgnored {
    private String album;

    private String artist;

    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Song clone() throws CloneNotSupportedException {
        Song clone = (Song) super.clone();
        clone.album = album;
        clone.artist = artist;
        clone.title = title;
        clone.url = url;
        return clone;
    }

    @Override
    public String toString() {
        return "ClassPojo [album = " + album + ", artist = " + artist + ", title = " + title + ", url = " + url + "]";
    }
}