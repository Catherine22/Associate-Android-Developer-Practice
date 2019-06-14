package com.catherine.materialdesignapp.open_weather.models;

public class Sys {
    private String country;

    private String sunrise;

    private String sunset;

    private String id;

    private String type;

    private String message;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClassPojo [country = " + country + ", sunrise = " + sunrise + ", sunset = " + sunset + ", id = " + id + ", type = " + type + ", message = " + message + "]";
    }
}

