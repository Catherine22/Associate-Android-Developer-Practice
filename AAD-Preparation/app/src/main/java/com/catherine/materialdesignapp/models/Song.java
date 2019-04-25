package com.catherine.materialdesignapp.models;

import com.catherine.materialdesignapp.listeners.ProguardIgnored;

public class Song implements ProguardIgnored {
    private String image;

    private String artist;

    private String title;

    private String url;

    private String thumbnail_image;

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

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    @Override
    public String toString() {
        return "ClassPojo [image = " + image + ", artist = " + artist + ", title = " + title + ", url = " + url + ", thumbnail_image = " + thumbnail_image + "]";
    }
}