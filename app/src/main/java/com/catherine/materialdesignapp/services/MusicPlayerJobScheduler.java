package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import com.catherine.materialdesignapp.tasks.MusicPlayer;

@TargetApi(Build.VERSION_CODES.O)
public class MusicPlayerJobScheduler extends JobService {
    private static final String TAG = MusicPlayerService.class.getSimpleName();
    private MusicPlayer musicPlayer;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        musicPlayer = new MusicPlayer();
        musicPlayer.play();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        musicPlayer.stop();
        return false;
    }
}
