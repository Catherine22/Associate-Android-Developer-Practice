package com.catherine.materialdesignapp.open_weather.models;

public class Main {
    private float temp;

    private float temp_min;

    private int humidity;

    private int pressure;

    private float temp_max;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    @Override
    public String toString() {
        return "ClassPojo [temp = " + temp + ", temp_min = " + temp_min + ", humidity = " + humidity + ", pressure = " + pressure + ", temp_max = " + temp_max + "]";
    }
}

