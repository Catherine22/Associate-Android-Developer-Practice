package com.catherine.materialdesignapp.open_weather.models;

public class Wind {
    private int deg;

    private float speed;

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "ClassPojo [deg = " + deg + ", speed = " + speed + "]";
    }
}
