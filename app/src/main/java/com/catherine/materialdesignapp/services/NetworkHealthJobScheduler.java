package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import com.catherine.materialdesignapp.receivers.InternetConnectivityReceiver;
import com.catherine.materialdesignapp.tasks.MusicPlayer;

@TargetApi(Build.VERSION_CODES.O)
public class NetworkHealthJobScheduler extends JobService {
    private static final String TAG = MusicPlayerService.class.getSimpleName();
    private InternetConnectivityReceiver internetReceiver;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        internetReceiver = new InternetConnectivityReceiver();
        IntentFilter internetIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetReceiver, internetIntentFilter);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        return false;
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(internetReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
