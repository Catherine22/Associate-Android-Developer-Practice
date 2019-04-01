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

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d(TAG, "ON_START");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.d(TAG, "ON_CREATE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.d(TAG, "ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.d(TAG, "ON_DESTROY");
    }
}
