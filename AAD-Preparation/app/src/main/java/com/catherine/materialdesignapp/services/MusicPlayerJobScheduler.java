package com.catherine.materialdesignapp.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import com.catherine.materialdesignapp.listeners.OnMusicPlayerListener;
import com.catherine.materialdesignapp.tasks.MusicPlayer;

@TargetApi(Build.VERSION_CODES.O)
public class MusicPlayerJobScheduler extends JobService implements OnMusicPlayerListener {
    private static final String TAG = MusicPlayerJobScheduler.class.getSimpleName();
    private MusicPlayer musicPlayer;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "onStartJob");
        this.jobParameters = jobParameters;
        musicPlayer = new MusicPlayer();
        musicPlayer.setOnMusicPlayerListener(this);
        musicPlayer.play();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "onStopJob");
        musicPlayer.stop();
        jobFinished(jobParameters, false);
        return false;
    }


    @Override
    public void onFinished() {
        Log.e(TAG, "onFinished");
        onStopJob(jobParameters);
    }
}
