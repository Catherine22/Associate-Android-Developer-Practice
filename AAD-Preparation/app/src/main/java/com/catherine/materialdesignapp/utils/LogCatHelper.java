package com.catherine.materialdesignapp.utils;

import android.os.AsyncTask;
import android.widget.TextView;
import com.catherine.materialdesignapp.tasks.LogcatTask;


public class LogCatHelper {

    public void setLogCatListener() {

    }

    public void startRecording(TextView textView) {
        new LogcatTask(textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
