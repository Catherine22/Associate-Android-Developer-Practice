package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.receivers.AirplaneModeChangedReceiver;

@TargetApi(Build.VERSION_CODES.O)
public class AirplaneModeJobScheduler extends JobService {
    private static final String TAG = AirplaneModeJobScheduler.class.getSimpleName();
    private AirplaneModeChangedReceiver airplaneModeChangedReceiver;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");

        airplaneModeChangedReceiver = new AirplaneModeChangedReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        MyApplication.INSTANCE.registerReceiver(airplaneModeChangedReceiver, intentFilter);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        try {
            MyApplication.INSTANCE.unregisterReceiver(airplaneModeChangedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jobFinished(jobParameters, false);
        return false;
    }
}
