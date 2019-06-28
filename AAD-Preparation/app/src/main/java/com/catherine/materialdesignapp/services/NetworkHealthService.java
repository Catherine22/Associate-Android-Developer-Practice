package com.catherine.materialdesignapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.catherine.materialdesignapp.listeners.ProguardIgnored;
import com.catherine.materialdesignapp.receivers.InternetConnectivityReceiver;


public class NetworkHealthService extends Service implements ProguardIgnored {
    private InternetConnectivityReceiver internetReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        internetReceiver = new InternetConnectivityReceiver();
        IntentFilter internetIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetReceiver, internetIntentFilter);
    }


    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(internetReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}