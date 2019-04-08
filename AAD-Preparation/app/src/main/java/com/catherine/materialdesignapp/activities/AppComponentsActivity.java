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
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.components.BottomSheetItem;
import com.catherine.materialdesignapp.content_providers.CallLogDao;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;
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
    //    private BottomSheetBehavior behavior;
    private BottomSheetDialog bottomSheetDialog;
    private CallLogDao callLogDao;

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


        // radio buttons
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


        // bottom sheet dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_app_components, null);
        bottomSheetDialog.setContentView(view);

        String[] titles = getResources().getStringArray(R.array.app_components_bottom_sheet_array);
        BottomSheetItem item_create = bottomSheetDialog.findViewById(R.id.item_create);
        item_create.setTitle(titles[0]);
        item_create.setOnClickListener(this);
        BottomSheetItem item_read = bottomSheetDialog.findViewById(R.id.item_read);
        item_read.setTitle(titles[1]);
        item_read.setOnClickListener(this);
        BottomSheetItem item_update = bottomSheetDialog.findViewById(R.id.item_update);
        item_update.setTitle(titles[2]);
        item_update.setOnClickListener(this);
        BottomSheetItem item_delete = bottomSheetDialog.findViewById(R.id.item_delete);
        item_delete.setTitle(titles[3]);
        item_delete.setOnClickListener(this);

        // bottom sheet another version
//        View bottomSheet = findViewById(R.id.bottom_sheet);
//        behavior = BottomSheetBehavior.from(bottomSheet);
//        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.d(TAG, String.format(Locale.US, "bottomSheet: %d", newState));
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            }
//        });
//        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        String[] titles = getResources().getStringArray(R.array.app_components_bottom_sheet_array);
//        TextView tv_create = findViewById(R.id.tv_create);
//        tv_create.setText(titles[0]);
//        tv_create.setOnClickListener(this);
//        TextView tv_read = findViewById(R.id.tv_read);
//        tv_read.setText(titles[1]);
//        tv_read.setOnClickListener(this);
//        TextView tv_update = findViewById(R.id.tv_update);
//        tv_update.setText(titles[2]);
//        tv_update.setOnClickListener(this);
//        TextView tv_delete = findViewById(R.id.tv_delete);
//        tv_delete.setText(titles[3]);
//        tv_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_service:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (selectedServiceType == ServiceType.BACKGROUND) {
                    startService(true);
                } else if (selectedServiceType == ServiceType.FOREGROUND) {
                    startService(false);
                } else {
                    scheduleJob();
                }
                break;
            case R.id.btn_stop_service:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (selectedServiceType == ServiceType.BACKGROUND) {
                    stopService();
                } else if (selectedServiceType == ServiceType.FOREGROUND) {
                    stopService();
                } else {
                    stopJob();
                }
                break;
            case R.id.btn_broadcast_receivers:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                // TODO add one more broadcast receiver
                break;
            case R.id.btn_content_providers:
                String[] permissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG};
                getPermissions(permissions, new OnRequestPermissionsListener() {
                    @Override
                    public void onGranted() {
//                        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
//                            callLogDao = new CallLogDao();
//                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        } else
//                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                        if (bottomSheetDialog.isShowing()) {
                            bottomSheetDialog.closeOptionsMenu();
                        } else {
                            callLogDao = new CallLogDao();
                            bottomSheetDialog.show();
                        }
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
            case R.id.item_create:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetDialog.closeOptionsMenu();
                break;
            case R.id.item_read:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                callLogDao.read();
                bottomSheetDialog.closeOptionsMenu();
                // TODO show call logs on RecyclerView
                break;
            case R.id.item_update:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetDialog.closeOptionsMenu();
                // TODO select call logs presented RecyclerView
                break;
            case R.id.item_delete:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetDialog.closeOptionsMenu();
                // TODO select call logs presented RecyclerView
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
