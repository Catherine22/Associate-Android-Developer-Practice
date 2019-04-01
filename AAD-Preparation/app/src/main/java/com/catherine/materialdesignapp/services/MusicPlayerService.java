package com.catherine.materialdesignapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.catherine.materialdesignapp.models.NotificationChannelsGroup;
import com.catherine.materialdesignapp.tasks.MusicPlayer;
import com.catherine.materialdesignapp.utils.NotificationUtils;

public class MusicPlayerService extends Service {
    private static final String TAG = MusicPlayerService.class.getSimpleName();
    private MusicPlayer musicPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils notificationUtils = new NotificationUtils(this, NotificationChannelsGroup.CHANNELS.get(NotificationChannelsGroup.FOREGROUND_SERVICE));
            startForeground(1, notificationUtils.getServiceNotification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        musicPlayer = new MusicPlayer();
        musicPlayer.play();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        musicPlayer.stop();
        super.onDestroy();
    }
}