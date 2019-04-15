package com.catherine.materialdesignapp.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class SleepTask extends AsyncTask<String, Integer, String> {
    private WeakReference<TextView> textView;
    private int SECONDS = 5;


    public SleepTask(TextView textView) {
        this.textView = new WeakReference<>(textView);
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            for (int i = 0; i < SECONDS; i++) {
                publishProgress(i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuilder result = new StringBuilder();
        if (strings != null) {
//            result = String.join(", ", strings);
            for (String s : strings) {
                result.append(s);
                result.append(" ");
            }

            result.deleteCharAt(result.length() - 1);
        } else {
            result = new StringBuilder("empty string");
        }
        return String.format(Locale.US, "New string: %s", result.toString());
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = (100 / SECONDS) * values[0];
        textView.get().setText(String.format(Locale.US, "Progress: %d%%", progress));
        super.onProgressUpdate(values);
    }

    protected void onPostExecute(String s) {
        textView.get().setText(s);
        super.onPostExecute(s);
    }
}