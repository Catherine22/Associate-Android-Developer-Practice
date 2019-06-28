package com.catherine.materialdesignapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.listeners.ProguardIgnored;
import com.catherine.materialdesignapp.receivers.BatteryLowReceiver;


public class BatteryLowService extends Service implements ProguardIgnored {
    public final static String TAG = BatteryLowService.class.getSimpleName();

    private BatteryLowReceiver batteryLowReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind:" + intent.toString());
        batteryLowReceiver = new BatteryLowReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        MyApplication.INSTANCE.registerReceiver(batteryLowReceiver, intentFilter);
        return null;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.d(TAG, "unbindService");try {
            MyApplication.INSTANCE.unregisterReceiver(batteryLowReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.unbindService(conn);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}