package com.catherine.materialdesignapp.open_weather.models;

public class WeatherResult {

    private int count;

    private String cod;

    private String message;

    private MyList[] list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MyList[] getList() {
        return list;
    }

    public void setList(MyList[] list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ClassPojo [count = " + count + ", cod = " + cod + ", message = " + message + ", list = " + list + "]";
    }
}
