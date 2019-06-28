package com.catherine.materialdesignapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import com.catherine.materialdesignapp.utils.NotificationUtils;

public class AirplaneModeChangedReceiver extends BroadcastReceiver {
    public final static String TAG = AirplaneModeChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        String subtitle = Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0
                ? "Disabled airplane mode"
                : "Enabled airplane mode";
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.sendNotification(TAG, subtitle, (int) System.currentTimeMillis() / 1000, Intent.ACTION_AIRPLANE_MODE_CHANGED);
    }
}
