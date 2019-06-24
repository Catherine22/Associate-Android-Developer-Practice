package com.catherine.materialdesignapp.tasks;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnMusicPlayerListener;

public class MusicPlayer implements MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private OnMusicPlayerListener onMusicPlayerListener;

    public MusicPlayer() {
        MyApplication.INSTANCE.musicPlayerThread = new HandlerThread("music player thread");
        MyApplication.INSTANCE.musicPlayerThread.start();
    }

    public void setOnMusicPlayerListener(OnMusicPlayerListener onMusicPlayerListener) {
        this.onMusicPlayerListener = onMusicPlayerListener;
    }

    public void play() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            return;

        Handler handler = new Handler(MyApplication.INSTANCE.musicPlayerThread.getLooper());
        handler.post(() -> {
            mediaPlayer = MediaPlayer.create(MyApplication.INSTANCE.getApplicationContext(), R.raw.breathing);

            // play the music
//            mediaPlayer.setOnCompletionListener(this);
//            mediaPlayer.start();


            // test
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onCompletion(mediaPlayer);
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (onMusicPlayerListener != null)
            onMusicPlayerListener.onFinished();
        stop();
    }
}
