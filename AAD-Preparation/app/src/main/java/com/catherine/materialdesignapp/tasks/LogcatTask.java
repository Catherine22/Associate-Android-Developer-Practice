package com.catherine.materialdesignapp.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import com.catherine.materialdesignapp.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

public class LogcatTask extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> textView;
    private String cache = "";

    public LogcatTask(TextView textView) {
        this.textView = new WeakReference<>(textView);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Process process = Runtime.getRuntime().exec(String.format("logcat -d %s:v -v brief", MyApplication.INSTANCE.getPackageName()));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            cache = log.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cache;
    }

    protected void onPostExecute(String s) {
        this.textView.get().setText(s);
        super.onPostExecute(s);
    }
}
