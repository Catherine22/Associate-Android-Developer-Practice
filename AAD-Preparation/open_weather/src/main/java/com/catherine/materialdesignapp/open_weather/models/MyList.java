package com.catherine.materialdesignapp.open_weather.models;

public class MyList {
    private String dt;

    private Rain rain;

    private Coord coord;

    private Snow snow;

    private String name;

    private Weather[] weather;

    private Main main;

    private String id;

    private Clouds clouds;

    private Sys sys;

    private Wind wind;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "ClassPojo [dt = " + dt + ", rain = " + rain + ", coord = " + coord + ", snow = " + snow + ", name = " + name + ", weather = " + weather + ", main = " + main + ", id = " + id + ", clouds = " + clouds + ", sys = " + sys + ", wind = " + wind + "]";
    }
}
