package com.catherine.materialdesignapp;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.HandlerThread;

import com.catherine.materialdesignapp.services.NetworkHealthJobScheduler;
import com.catherine.materialdesignapp.services.NetworkHealthService;

import static com.catherine.materialdesignapp.services.BusyJobs.JOB_NETWORK_STAYE;

public class MyApplication extends Application {
    public static MyApplication INSTANCE;
    public HandlerThread musicPlayerThread;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        // start network monitoring service
        Intent intent = new Intent(INSTANCE, NetworkHealthService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                ComponentName componentName = new ComponentName(this, NetworkHealthJobScheduler.class);
                JobInfo jobInfo = new JobInfo.Builder(JOB_NETWORK_STAYE, componentName)
                        .setRequiresStorageNotLow(false)
                        .setRequiresBatteryNotLow(true)
                        .setRequiresCharging(false)
                        .build();
                jobScheduler.schedule(jobInfo);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            startService(intent);
        }
    }
}
