package com.catherine.materialdesignapp.tasks;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public MusicPlayer() {
        MyApplication.INSTANCE.musicPlayerThread = new HandlerThread("music player thread");
        MyApplication.INSTANCE.musicPlayerThread.start();
    }

    public void play() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            return;

        Handler handler = new Handler(MyApplication.INSTANCE.musicPlayerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(MyApplication.INSTANCE.getApplicationContext(), R.raw.breathing);
                mediaPlayer.start();
            }
        });
    }

    public void stop() {
        if (mediaPlayer == null)
            return;
        mediaPlayer.release();
        mediaPlayer = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (MyApplication.INSTANCE.musicPlayerThread != null && MyApplication.INSTANCE.musicPlayerThread.isAlive())
                MyApplication.INSTANCE.musicPlayerThread.quitSafely();
            else
                MyApplication.INSTANCE.musicPlayerThread.quit();
        }
    }
}
