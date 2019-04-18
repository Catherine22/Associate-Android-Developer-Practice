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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.services.MusicPlayerJobScheduler;
import com.catherine.materialdesignapp.services.MusicPlayerService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import static com.catherine.materialdesignapp.services.BusyJobs.JOB_MUSIC_PLAYER;

public class BackgroundServiceFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = BackgroundServiceFragment.class.getSimpleName();
    private RadioGroup rb_network;
    private Switch switch_require_network, switch_battery_not_low, switch_charging, switch_device_idle;
    private AppCompatSeekBar sb_deadline;
    private TextView tv_deadline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_background_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_start_service = view.findViewById(R.id.btn_start_service);
        btn_start_service.setOnClickListener(this);
        Button btn_stop_service = view.findViewById(R.id.btn_stop_service);
        btn_stop_service.setOnClickListener(this);
        LinearLayout layout_android_o = view.findViewById(R.id.ll_oreo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            btn_start_service.setVisibility(View.GONE);
            btn_stop_service.setVisibility(View.GONE);
            layout_android_o.setVisibility(View.VISIBLE);

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
            switch_battery_not_low = view.findViewById(R.id.switch_battery_not_low);
            switch_charging = view.findViewById(R.id.switch_charging);
            switch_device_idle = view.findViewById(R.id.switch_device_idle);
            sb_deadline = view.findViewById(R.id.sb_deadline);

            int PROGRESS_MIN = 0;
            int PROGRESS_MAX = 100;
            sb_deadline.setMax(PROGRESS_MAX);
            tv_deadline = view.findViewById(R.id.tv_deadline);
            tv_deadline.setText(String.format(getResources().getQuantityString(R.plurals.second, PROGRESS_MIN), PROGRESS_MIN));
            sb_deadline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tv_deadline.setText(String.format(getResources().getQuantityString(R.plurals.second, progress), progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } else {
            btn_start_service.setVisibility(View.VISIBLE);
            btn_stop_service.setVisibility(View.VISIBLE);
            layout_android_o.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        switch (v.getId()) {
            case R.id.btn_start_service:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                getActivity().startService(intent);
                break;
            case R.id.btn_stop_service:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                getActivity().stopService(intent);
                break;
            case R.id.btn_start_job:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
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
                                    requiresNetwork = JobInfo.NETWORK_TYPE_CELLULAR;
                                    break;
                            }
                        }

                        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(getActivity().JOB_SCHEDULER_SERVICE);
                        ComponentName componentName = new ComponentName(getActivity(), MusicPlayerJobScheduler.class);
                        JobInfo jobInfo = new JobInfo.Builder(JOB_MUSIC_PLAYER, componentName)
                                .setRequiresDeviceIdle(switch_device_idle.isChecked())
                                .setRequiresBatteryNotLow(switch_battery_not_low.isChecked())
                                .setRequiresCharging(switch_charging.isChecked())
                                .setRequiredNetworkType(requiresNetwork)
                                .setBackoffCriteria(1000 * sb_deadline.getProgress(), JobInfo.BACKOFF_POLICY_LINEAR)
                                .build();
                        jobScheduler.schedule(jobInfo);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_stop_job:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(getActivity().JOB_SCHEDULER_SERVICE);
                        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                            if (jobInfo.getId() == JOB_MUSIC_PLAYER) {
                                jobScheduler.cancel(JOB_MUSIC_PLAYER);
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
