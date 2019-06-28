package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.receivers.ScreenOnReceiver;

@TargetApi(Build.VERSION_CODES.O)
public class ScreenOnOffJobScheduler extends JobService {
    private static final String TAG = ScreenOnOffJobScheduler.class.getSimpleName();
    private ScreenOnReceiver screenOnReceiver;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        screenOnReceiver = new ScreenOnReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        MyApplication.INSTANCE.registerReceiver(screenOnReceiver, intentFilter);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        try {
            MyApplication.INSTANCE.unregisterReceiver(screenOnReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jobFinished(jobParameters, false);
        return false;
    }
}
