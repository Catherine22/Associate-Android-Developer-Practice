package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import com.catherine.materialdesignapp.listeners.OnMusicPlayerListener;
import com.catherine.materialdesignapp.receivers.ScreenOnReceiver;

@TargetApi(Build.VERSION_CODES.O)
public class ScreenOnOffJobScheduler extends JobService implements OnMusicPlayerListener {
    private static final String TAG = ScreenOnOffJobScheduler.class.getSimpleName();
    private JobParameters jobParameters;
    private ScreenOnReceiver screenOnReceiver;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        this.jobParameters = jobParameters;

        screenOnReceiver = new ScreenOnReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOnReceiver, intentFilter);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        unregisterReceiver(screenOnReceiver);
        jobFinished(jobParameters, false);
        return false;
    }


    @Override
    public void onFinished() {
        Log.e(TAG, "onFinished");
        onStopJob(jobParameters);
    }
}
