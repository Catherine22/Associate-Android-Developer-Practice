package com.catherine.materialdesignapp.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.catherine.materialdesignapp.R;

import java.util.Locale;

public class SleepTaskLoader extends AsyncTaskLoader<String> {
    public final static String TAG = SleepTaskLoader.class.getSimpleName();
    private int SECONDS = 5;
    private String text;
    private String cache;
    private String newString, emptyString, progressInt, canceledInt;

    public SleepTaskLoader(Context context, String text) {
        super(context);
        this.text = text;
        newString = context.getString(R.string.new_string);
        emptyString = context.getString(R.string.empty_string);
        progressInt = context.getString(R.string.progress_int);
        canceledInt = context.getString(R.string.canceled_int);
    }

    @Override
    public String loadInBackground() {
        try {
            for (int i = 0; i < SECONDS; i++) {
                int progress = (100 / SECONDS) * i;
                deliverResult(String.format(Locale.US, progressInt, progress));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = (TextUtils.isEmpty(text)) ? emptyString : text;
        return String.format(Locale.US, newString, result);
    }

    @Override
    protected void onStartLoading() {
        Log.i(TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public void stopLoading() {
        Log.i(TAG, "stopLoading");
        deliverResult(String.format(Locale.US, canceledInt, cache));
        super.stopLoading();
    }

    @Override
    public void deliverResult(String data) {
        Log.i(TAG, String.format(Locale.US, "deliverResult: %s", data));
        cache = data;
        if (!isReset() && isStarted()) {
            super.deliverResult(cache);
        }
    }
}
