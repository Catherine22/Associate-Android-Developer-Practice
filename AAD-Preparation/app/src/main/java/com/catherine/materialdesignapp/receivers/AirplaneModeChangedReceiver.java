package com.catherine.materialdesignapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.catherine.materialdesignapp.models.NotificationChannelsGroup;
import com.catherine.materialdesignapp.utils.NotificationUtils;

public class AirplaneModeChangedReceiver extends BroadcastReceiver {
    public final static String TAG = AirplaneModeChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        String subtitle = Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0
                ? "Disabled airplane mode"
                : "Enabled airplane mode";
        NotificationUtils notificationUtils;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationUtils = new NotificationUtils(context, NotificationChannelsGroup.CHANNELS.get(NotificationChannelsGroup.SYSTEM_RECEIVERS));
        else
            notificationUtils = new NotificationUtils(context);
        notificationUtils.sendNotification(TAG, subtitle, (int) System.currentTimeMillis() / 1000, Intent.ACTION_AIRPLANE_MODE_CHANGED);
    }
}
