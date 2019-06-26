package com.catherine.materialdesignapp.utils;

import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.catherine.materialdesignapp.listeners.LifecycleListener;

public class LifecycleObserverImpl implements LifecycleObserver {
    public final static String TAG = LifecycleObserverImpl.class.getSimpleName();
    private LifecycleListener lifecycleListener;


    public void setLifecycleListener(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        if (lifecycleListener != null)
            lifecycleListener.onChanged("ON_RESUME");
        Log.d(TAG, "ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disconnectListener() {
        if (lifecycleListener != null)
            lifecycleListener.onChanged("ON_STOP");
        Log.d(TAG, "ON_STOP");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (lifecycleListener != null)
            lifecycleListener.onChanged("ON_START");
        Log.d(TAG, "ON_START");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        if (lifecycleListener != null)
            lifecycleListener.onChanged("ON_CREATE");
        Log.d(TAG, "ON_CREATE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (lifecycleListener != null)
            lifecycleListener.onChanged("ON_PAUSE");
        Log.d(TAG, "ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (lifecycleListener != null)
            lifecycleListener.onChanged("ON_DESTROY");
        Log.d(TAG, "ON_DESTROY");
    }
}
