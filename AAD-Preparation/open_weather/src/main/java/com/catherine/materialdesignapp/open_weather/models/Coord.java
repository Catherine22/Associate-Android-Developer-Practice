package com.catherine.materialdesignapp.open_weather.models;

public class Coord {
    private float lon;

    private float lat;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Class [lon = " + lon + ", lat = " + lat + "]";
    }
}