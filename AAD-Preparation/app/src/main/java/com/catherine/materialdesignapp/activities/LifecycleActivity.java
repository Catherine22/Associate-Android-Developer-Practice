package com.catherine.materialdesignapp.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.LifecycleAdapter;
import com.catherine.materialdesignapp.listeners.LifecycleListener;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.services.BusyJobs;
import com.catherine.materialdesignapp.services.LogcatJobScheduler;
import com.catherine.materialdesignapp.services.LogcatService;
import com.catherine.materialdesignapp.utils.LifecycleObserverImpl;
import com.catherine.materialdesignapp.utils.OccupiedActions;
import com.catherine.messagecenter.Client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LifecycleActivity extends BaseActivity implements LifecycleOwner, LifecycleListener {
    public final static String TAG = LifecycleActivity.class.getSimpleName();
    private final static String STATE_TIMESTAMP = "timestamp";
    private final static String STATE_LOG = "log";
    private final static String STATE_LIFECYCLE_EVENTS = "lifecycleEvents";
    private List<Pair<String, String>> features = new ArrayList<>();
    private LifecycleAdapter adapter;
    private Client client;

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

        LifecycleObserverImpl lifecycleObserver = new LifecycleObserverImpl();
        lifecycleObserver.setLifecycleListener(this);
        getLifecycle().addObserver(lifecycleObserver);

        features.add(new Pair<>(getString(R.string.logcat), ""));
        features.add(new Pair<>(getString(R.string.lifecycle), ""));

        RecyclerView rv_features = findViewById(R.id.rv_features);
        rv_features.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LifecycleAdapter(this, features, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                boolean newState = !adapter.isUnfolded(position);
                adapter.unfold(position, newState);
                adapter.dropDownOrUp(view, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv_features.setAdapter(adapter);

        client = new Client(LifecycleActivity.this, result -> updateEntity(0, result.getString()));
        client.gotMessages(OccupiedActions.ACTION_UPDATE_LOGGER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                ComponentName componentName = new ComponentName(this, LogcatJobScheduler.class);
                JobInfo jobInfo = new JobInfo.Builder(BusyJobs.JOB_LOGGER, componentName)
                        .setRequiresStorageNotLow(true)
                        .setRequiresCharging(false)
                        .build();
                jobScheduler.schedule(jobInfo);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(this, LogcatService.class);
            startService(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        long timestamp = System.currentTimeMillis();
        savedInstanceState.putLong(STATE_TIMESTAMP, timestamp);
        savedInstanceState.putString(STATE_LOG, features.get(0).second);
        savedInstanceState.putString(STATE_LIFECYCLE_EVENTS, features.get(1).second);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null)
            return;
        long savedTimestamp = savedInstanceState.getLong(STATE_TIMESTAMP);
        features.set(0, new Pair<>(features.get(0).first, savedInstanceState.getString(STATE_LOG)));
        features.set(1, new Pair<>(features.get(1).first, savedInstanceState.getString(STATE_LIFECYCLE_EVENTS)));
        Log.e(TAG, String.format("restored saved instance state: %s", stampToDate(savedTimestamp)));
    }

    private String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    @Override
    public void onChanged(String state) {
        updateEntity(1, state);
    }

    @Override
    public void onDestroy() {
        if (client != null) {
            client.release();
        }
        super.onDestroy();
    }


    private void updateEntity(int pos, String str) {
        String content = adapter.getEntities().get(pos).second;
        adapter.updateContent(pos, content + "\n" + str);
    }
}
