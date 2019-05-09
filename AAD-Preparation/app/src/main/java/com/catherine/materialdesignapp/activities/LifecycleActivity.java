package com.catherine.materialdesignapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.LogCatListener;
import com.catherine.materialdesignapp.utils.LifecycleObserverImpl;
import com.catherine.materialdesignapp.utils.LogCatHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LifecycleActivity extends BaseActivity implements LifecycleOwner, LogCatListener {
    private final static String TAG = LifecycleActivity.class.getSimpleName();
    private final static String STATE_TIMESTAMP = "timestamp";
    private long savedTimestamp;
    private TextView tv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }

        getLifecycle().addObserver(new LifecycleObserverImpl());

        tv_log = findViewById(R.id.tv_log);


//        TextView textView = findViewById(R.id.tv_content);
        tv_log.setText(String.format(Locale.US, "Untranslatable strings: \n%s\n%s\n%s\n%s\n%s\n%s",
                getString(R.string.countdown), getString(R.string.star_rating),
                getString(R.string.app_homeurl), getString(R.string.prod_name),
                getString(R.string.promo_message), getResources().getQuantityString(R.plurals.numberOfSongsAvailable, 3)));

        LogCatHelper logCatHelper = new LogCatHelper();
        logCatHelper.startRecording(tv_log);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        long timestamp = System.currentTimeMillis();
        savedInstanceState.putLong(STATE_TIMESTAMP, timestamp);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null)
            return;
        savedTimestamp = savedInstanceState.getLong(STATE_TIMESTAMP);
        Log.e(TAG, String.format("restored saved instance state: %s", stampToDate(savedTimestamp)));
    }

    private String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    @Override
    public void onLog(String log) {
        tv_log.setText(log);
    }
}
