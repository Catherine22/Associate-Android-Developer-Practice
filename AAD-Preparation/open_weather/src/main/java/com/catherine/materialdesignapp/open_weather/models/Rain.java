package com.catherine.materialdesignapp.open_weather.models;

public class Rain {
    private int oneHour;

    private int threeHours;

    public int getOneHour() {
        return oneHour;
    }

    public void setOneHour(int oneHour) {
        this.oneHour = oneHour;
    }

    public int getThreeHours() {
        return threeHours;
    }

    public void setThreeHours(int threeHours) {
        this.threeHours = threeHours;
    }

    @Override
    public String toString() {
        return "Class [1H = " + oneHour + ", 3H = " + threeHours + "]";
    }
}