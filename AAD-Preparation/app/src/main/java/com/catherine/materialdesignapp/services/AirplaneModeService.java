package com.catherine.materialdesignapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.listeners.ProguardIgnored;
import com.catherine.materialdesignapp.receivers.AirplaneModeChangedReceiver;


public class AirplaneModeService extends Service implements ProguardIgnored {

    private AirplaneModeChangedReceiver airplaneModeChangedReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        airplaneModeChangedReceiver = new AirplaneModeChangedReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneModeChangedReceiver, intentFilter);
    }


    @Override
    public void onDestroy() {
        try {
            MyApplication.INSTANCE.unregisterReceiver(airplaneModeChangedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}