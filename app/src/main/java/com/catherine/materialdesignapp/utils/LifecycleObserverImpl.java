package com.catherine.materialdesignapp.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class LifecycleObserverImpl implements LifecycleObserver {
    private final static String TAG = LifecycleObserverImpl.class.getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        Log.d(TAG, "ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disconnectListener() {
        Log.d(TAG, "ON_STOP");
    }
}
