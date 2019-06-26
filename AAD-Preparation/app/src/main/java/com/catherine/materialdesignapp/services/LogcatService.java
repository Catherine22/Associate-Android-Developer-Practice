package com.catherine.materialdesignapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.catherine.materialdesignapp.listeners.ProguardIgnored;
import com.catherine.materialdesignapp.utils.LogCatHelper;


public class LogcatService extends Service implements ProguardIgnored {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogCatHelper logcatHelper = new LogCatHelper();
        logcatHelper.startRecording();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}