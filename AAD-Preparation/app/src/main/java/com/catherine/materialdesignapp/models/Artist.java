package com.catherine.materialdesignapp.models;

public class Artist {
    private String image;

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
        return "ClassPojo [image = " + image + ", artist = " + artist + ", url = " + url + "]";
    }
}