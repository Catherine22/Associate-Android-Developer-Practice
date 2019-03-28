package com.catherine.materialdesignapp.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.content_providers.CallLogDao;
import com.catherine.materialdesignapp.content_providers.UserDictionaryDao;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;
import com.catherine.materialdesignapp.models.Word;
import com.catherine.materialdesignapp.services.MusicPlayerJobScheduler;
import com.catherine.materialdesignapp.services.MusicPlayerService;

import java.util.List;

import static com.catherine.materialdesignapp.services.BusyJobs.JOB_MUSIC_PLAYER;

public class AppComponentsActivity extends BaseActivity implements OnClickListener {
    public final static String TAG = AppComponentsActivity.class.getSimpleName();

    private enum ServiceType {
        FOREGROUND, BACKGROUND, JOB_SCHEDULER
    }

    private ServiceType selectedServiceType = ServiceType.BACKGROUND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_components);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area

        final Button btn_start_service = findViewById(R.id.btn_start_service);
        btn_start_service.setOnClickListener(this);
        Button btn_stop_service = findViewById(R.id.btn_stop_service);
        btn_stop_service.setOnClickListener(this);
        Button btn_broadcast_receivers = findViewById(R.id.btn_broadcast_receivers);
        btn_broadcast_receivers.setOnClickListener(this);
        Button btn_content_providers = findViewById(R.id.btn_content_providers);
        btn_content_providers.setOnClickListener(this);


        RadioGroup rg_service_switch = findViewById(R.id.rg_service_switch);
        RadioButton rbn_foreground_service = findViewById(R.id.rbn_foreground_service);
        RadioButton rbn_job_scheduler = findViewById(R.id.rbn_job_scheduler);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            rbn_foreground_service.setClickable(true);
            rbn_job_scheduler.setClickable(true);
        } else {
            rbn_foreground_service.setText(getString(R.string.background_service));
            rbn_foreground_service.setClickable(false);
            rbn_job_scheduler.setClickable(false);
            selectedServiceType = ServiceType.BACKGROUND;
        }
        rg_service_switch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbn_foreground_service) {
                    selectedServiceType = ServiceType.FOREGROUND;
                } else {
                    selectedServiceType = ServiceType.JOB_SCHEDULER;
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_service:
                if (selectedServiceType == ServiceType.BACKGROUND) {
                    startService(true);
                } else if (selectedServiceType == ServiceType.FOREGROUND) {
                    startService(false);
                } else {
                    scheduleJob();
                }
                break;
            case R.id.btn_stop_service:
                if (selectedServiceType == ServiceType.BACKGROUND) {
                    stopService();
                } else if (selectedServiceType == ServiceType.FOREGROUND) {
                    stopService();
                } else {
                    stopJob();
                }
                break;
            case R.id.btn_broadcast_receivers:
                // TODO add one more broadcast receiver
                break;
            case R.id.btn_content_providers:
//                UserDictionaryDao dao = new UserDictionaryDao();
//                dao.query();
//
//                Word word = new Word("new word", 999, "new shortcut", "en_US");
//                dao.insert(word);

                String[] permissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG};
                getPermissions(permissions, new OnRequestPermissionsListener() {
                    @Override
                    public void onGranted() {
                        CallLogDao callLogDao = new CallLogDao();
                        callLogDao.read();
                    }

                    @Override
                    public void onDenied(@Nullable List<String> deniedPermissions) {
                        Log.d(TAG, "onDenied");
                    }

                    @Override
                    public void onRetry() {
                        Log.d(TAG, "onRetry");
                    }
                });
                break;
        }
    }

    private void startService(boolean isBackground) {
        Intent intent = new Intent(this, MusicPlayerService.class);
        if (isBackground)
            startService(intent);
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }
        }
    }

    private void stopService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        stopService(intent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void scheduleJob() {
        try {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName componentName = new ComponentName(this, MusicPlayerJobScheduler.class);
            JobInfo jobInfo = new JobInfo.Builder(JOB_MUSIC_PLAYER, componentName)
                    .setRequiresStorageNotLow(false)
                    .setRequiresBatteryNotLow(true)
                    .setRequiresCharging(false)
                    .build();
            jobScheduler.schedule(jobInfo);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void stopJob() {
        try {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == JOB_MUSIC_PLAYER) {
                    jobScheduler.cancel(JOB_MUSIC_PLAYER);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
