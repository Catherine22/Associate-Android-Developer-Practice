package com.catherine.materialdesignapp;

import android.app.Application;
import android.os.HandlerThread;

public class MyApplication extends Application {
    public static MyApplication INSTANCE;
    public HandlerThread musicPlayerThread;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
