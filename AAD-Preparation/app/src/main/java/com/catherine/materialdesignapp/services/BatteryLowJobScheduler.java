package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.receivers.BatteryLowReceiver;

@TargetApi(Build.VERSION_CODES.O)
public class BatteryLowJobScheduler extends JobService {
    private static final String TAG = BatteryLowJobScheduler.class.getSimpleName();
    private BatteryLowReceiver batteryLowReceiver;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        batteryLowReceiver = new BatteryLowReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        MyApplication.INSTANCE.registerReceiver(batteryLowReceiver, intentFilter);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        try {
            MyApplication.INSTANCE.unregisterReceiver(batteryLowReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jobFinished(jobParameters, false);
        return false;
    }
}
