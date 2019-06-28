package com.catherine.materialdesignapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.listeners.ProguardIgnored;
import com.catherine.materialdesignapp.receivers.ScreenOnReceiver;


public class ScreenOnOffService extends Service implements ProguardIgnored {

    private ScreenOnReceiver screenOnReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        screenOnReceiver = new ScreenOnReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        MyApplication.INSTANCE.registerReceiver(screenOnReceiver, intentFilter);
    }


    @Override
    public void onDestroy() {
        try {
            MyApplication.INSTANCE.unregisterReceiver(screenOnReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}