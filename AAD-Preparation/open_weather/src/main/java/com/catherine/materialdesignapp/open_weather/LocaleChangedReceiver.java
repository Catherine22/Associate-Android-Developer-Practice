package com.catherine.materialdesignapp.open_weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocaleChangedReceiver extends BroadcastReceiver {
    public final static String TAG = LocaleChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent.getAction());
    }
}
