package com.catherine.materialdesignapp.fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.components.Slider;
import com.catherine.materialdesignapp.listeners.OnActivityEventListener;
import com.catherine.materialdesignapp.services.MusicPlayerJobScheduler;
import com.catherine.materialdesignapp.services.MusicPlayerService;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.catherine.materialdesignapp.services.BusyJobs.JOB_MUSIC_PLAYER;

public class BackgroundServiceFragment extends Fragment implements View.OnClickListener {
    public final static String TAG = BackgroundServiceFragment.class.getSimpleName();
    private JobScheduler jobScheduler;
    private RadioGroup rb_network;
    private Switch switch_require_network, switch_battery_not_low, switch_charging, switch_device_idle;
    private Slider slider_latency, slider_deadline;
    private OnActivityEventListener onActivityEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_background_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Button btn_start_job = view.findViewById(R.id.btn_start_job);
            btn_start_job.setOnClickListener(this);
            Button btn_stop_job = view.findViewById(R.id.btn_stop_job);
            btn_stop_job.setOnClickListener(this);
            rb_network = view.findViewById(R.id.rb_network);
            rb_network.setVisibility(View.GONE);
            switch_require_network = view.findViewById(R.id.switch_require_network);
            switch_require_network.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    rb_network.setVisibility(View.VISIBLE);
                } else {
                    rb_network.setVisibility(View.GONE);
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                switch_battery_not_low = view.findViewById(R.id.switch_battery_not_low);
            switch_charging = view.findViewById(R.id.switch_charging);
            switch_device_idle = view.findViewById(R.id.switch_device_idle);
            slider_deadline = view.findViewById(R.id.slider_deadline);
            slider_latency = view.findViewById(R.id.slider_latency);

            int PROGRESS_MIN = 0;
            int PROGRESS_MAX = 100;
            slider_deadline.setMaxProgress(PROGRESS_MAX);
            slider_deadline.setProgressDesc(String.format(getResources().getQuantityString(R.plurals.second, PROGRESS_MIN), PROGRESS_MIN));
            slider_deadline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    slider_deadline.setProgressDesc(String.format(getResources().getQuantityString(R.plurals.second, progress), progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            slider_latency.setMaxProgress(PROGRESS_MAX);
            slider_latency.setProgressDesc(String.format(getResources().getQuantityString(R.plurals.second, PROGRESS_MIN), PROGRESS_MIN));
            slider_latency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    slider_latency.setProgressDesc(String.format(getResources().getQuantityString(R.plurals.second, progress), progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            jobScheduler = (JobScheduler) MyApplication.INSTANCE.getSystemService(JOB_SCHEDULER_SERVICE);
        } else {
            Button btn_start_service = view.findViewById(R.id.btn_start_service);
            btn_start_service.setOnClickListener(this);
            Button btn_stop_service = view.findViewById(R.id.btn_stop_service);
            btn_stop_service.setOnClickListener(this);
        }

        onActivityEventListener = (OnActivityEventListener) getActivity();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        switch (v.getId()) {
            case R.id.btn_start_service:
                getActivity().startService(intent);
                break;
            case R.id.btn_stop_service:
                getActivity().stopService(intent);
                break;
            case R.id.btn_start_job:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int requiresNetwork = JobInfo.NETWORK_TYPE_NONE;
                    if (!switch_require_network.isChecked()) {
                        // RadioGroup
                        int radioButtonId = rb_network.getCheckedRadioButtonId();
                        switch (radioButtonId) {
                            case R.id.radio_any:
                                requiresNetwork = JobInfo.NETWORK_TYPE_ANY;
                                break;
                            case R.id.radio_unmetered:
                                // This device is connected to Wi-Fi but not a Hotspot
                                requiresNetwork = JobInfo.NETWORK_TYPE_UNMETERED;
                                break;
                            case R.id.radio_cellular:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    requiresNetwork = JobInfo.NETWORK_TYPE_CELLULAR;
                                }
                                break;
                        }
                    }
                    try {
                        ComponentName componentName = new ComponentName(getActivity(), MusicPlayerJobScheduler.class);
                        JobInfo jobInfo;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            jobInfo = new JobInfo.Builder(JOB_MUSIC_PLAYER, componentName)
                                    .setRequiresDeviceIdle(switch_device_idle.isChecked())
                                    .setRequiresBatteryNotLow(switch_battery_not_low.isChecked())
                                    .setRequiresCharging(switch_charging.isChecked())
                                    .setRequiredNetworkType(requiresNetwork)
                                    .setMinimumLatency(1000 * slider_latency.getProgress())
                                    .setBackoffCriteria(1000 * slider_deadline.getProgress(), JobInfo.BACKOFF_POLICY_LINEAR)
                                    .build();
                        } else {
                            jobInfo = new JobInfo.Builder(JOB_MUSIC_PLAYER, componentName)
                                    .setRequiresDeviceIdle(switch_device_idle.isChecked())
                                    .setRequiresCharging(switch_charging.isChecked())
                                    .setRequiredNetworkType(requiresNetwork)
                                    .setMinimumLatency(1000 * slider_latency.getProgress())
                                    .setBackoffCriteria(1000 * slider_deadline.getProgress(), JobInfo.BACKOFF_POLICY_LINEAR)
                                    .build();
                        }
                        jobScheduler.schedule(jobInfo);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        onActivityEventListener.showSnackbar(getView(), e.getMessage());
                    }
                }
                break;
            case R.id.btn_stop_job:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                            if (jobInfo.getId() == JOB_MUSIC_PLAYER) {
                                jobScheduler.cancel(JOB_MUSIC_PLAYER);
                                onActivityEventListener.showSnackbar(getView(), "Canceled");
                            }
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
