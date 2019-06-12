package com.catherine.materialdesignapp.utils;

public class CBridge {
    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();

    public native int plus(int x, int y);
}
