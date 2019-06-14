package com.catherine.materialdesignapp.open_weather.models;

public class Main {
    private String temp;

    private String temp_min;

    private String humidity;

    private String pressure;

    private String temp_max;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    @Override
    public String toString() {
        return "ClassPojo [temp = " + temp + ", temp_min = " + temp_min + ", humidity = " + humidity + ", pressure = " + pressure + ", temp_max = " + temp_max + "]";
    }
}

