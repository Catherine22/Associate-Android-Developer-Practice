package com.catherine.materialdesignapp.open_weather.models;

public class Sys {
    private String country;

    private int sunrise;

    private int sunset;

    private int id;

    private int type;

    private float message;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getMessage() {
        return message;
    }

    public void setMessage(float message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Class [country = " + country + ", sunrise = " + sunrise + ", sunset = " + sunset + ", id = " + id + ", type = " + type + ", message = " + message + "]";
    }
}

