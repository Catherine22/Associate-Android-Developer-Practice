package com.catherine.materialdesignapp.tasks;

import android.os.AsyncTask;
import android.widget.TextView;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class SleepTask extends AsyncTask<String, Integer, String> {
    private WeakReference<TextView> textView;
    private int SECONDS = 5;
    private String newString, emptyString, progressInt, canceledInt;


    public SleepTask(TextView textView) {
        this.textView = new WeakReference<>(textView);
        newString = MyApplication.INSTANCE.getString(R.string.new_string);
        emptyString = MyApplication.INSTANCE.getString(R.string.empty_string);
        progressInt = MyApplication.INSTANCE.getString(R.string.progress_int);
        canceledInt = MyApplication.INSTANCE.getString(R.string.canceled_int);
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
            result = new StringBuilder(emptyString);
        }
        return String.format(Locale.US, newString, result.toString());
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = (100 / SECONDS) * values[0];
        textView.get().setText(String.format(Locale.US, progressInt, progress));
        super.onProgressUpdate(values);
    }

    protected void onPostExecute(String s) {
        textView.get().setText(s);
        super.onPostExecute(s);
    }

    @Override
    protected void onCancelled() {
        textView.get().setText(String.format(canceledInt, textView.get().getText().toString()));
        super.onCancelled();
    }
}