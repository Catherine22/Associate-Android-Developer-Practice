package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;
import com.catherine.materialdesignapp.utils.LogCatHelper;

@TargetApi(Build.VERSION_CODES.O)
public class LogcatJobScheduler extends JobService {
    private static final String TAG = LogcatJobScheduler.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        LogCatHelper logcatHelper = new LogCatHelper();
        logcatHelper.startRecording();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        jobFinished(jobParameters, false);
        return false;
    }
}
