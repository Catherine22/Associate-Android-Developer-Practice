package com.catherine.materialdesignapp.open_weather.models;

public class Wind {
    private float deg;

    private float speed;

    public float getDeg() {
        return deg;
    }

    public void setDeg(float deg) {
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
        return "Class [deg = " + deg + ", speed = " + speed + "]";
    }
}
