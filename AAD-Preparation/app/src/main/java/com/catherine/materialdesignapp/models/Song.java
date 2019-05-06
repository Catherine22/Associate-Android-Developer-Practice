package com.catherine.materialdesignapp.models;

import com.catherine.materialdesignapp.listeners.ProguardIgnored;

public class Song implements Cloneable, ProguardIgnored {
    private String album;

    private String title;

    private String url;


    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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
        clone.title = title;
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
        return album == song.getAlbum() &&
                title == song.getTitle() &&
                url == song.getUrl();
    }

    @Override
    public String toString() {
        return "{album = " + album + ", title = " + title + ", url = " + url + "}";
    }
}