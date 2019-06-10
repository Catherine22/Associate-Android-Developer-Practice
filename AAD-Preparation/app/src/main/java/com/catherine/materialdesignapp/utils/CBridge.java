package com.catherine.materialdesignapp.utils;

/**
 * Created by catherine_chen on 2019-06-10.
 * Trend Micro
 * catherine_chen@trendmicro.com
 */
public class CBridge {
    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();

    public native int plus(int x, int y);
}
