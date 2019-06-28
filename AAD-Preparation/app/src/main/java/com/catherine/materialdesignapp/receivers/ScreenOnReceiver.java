package com.catherine.materialdesignapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.catherine.materialdesignapp.models.NotificationChannelsGroup;
import com.catherine.materialdesignapp.utils.NotificationUtils;

public class ScreenOnReceiver extends BroadcastReceiver {
    public final static String TAG = ScreenOnReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isScreenOn = Intent.ACTION_SCREEN_ON.equals(intent.getAction());
        String subtitle = isScreenOn ? "Screen On" : "Screen Off";
        NotificationUtils notificationUtils;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationUtils = new NotificationUtils(context, NotificationChannelsGroup.CHANNELS.get(NotificationChannelsGroup.SYSTEM_RECEIVERS));
        else
            notificationUtils = new NotificationUtils(context);
        notificationUtils.sendNotification(TAG, subtitle, (int) System.currentTimeMillis() / 1000, Intent.ACTION_SCREEN_ON);
    }
}
