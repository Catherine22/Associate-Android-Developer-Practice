package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;
import com.catherine.materialdesignapp.listeners.OnMusicPlayerListener;
import com.catherine.materialdesignapp.utils.LogCatHelper;

@TargetApi(Build.VERSION_CODES.O)
public class LogcatJobScheduler extends JobService implements OnMusicPlayerListener {
    private static final String TAG = LogcatJobScheduler.class.getSimpleName();
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        this.jobParameters = jobParameters;
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


    @Override
    public void onFinished() {
        Log.e(TAG, "onFinished");
        onStopJob(jobParameters);
    }
}
