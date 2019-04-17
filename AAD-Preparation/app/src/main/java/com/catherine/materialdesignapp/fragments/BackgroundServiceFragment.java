package com.catherine.materialdesignapp.fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.services.MusicPlayerJobScheduler;
import com.catherine.materialdesignapp.services.MusicPlayerService;

import static com.catherine.materialdesignapp.services.BusyJobs.JOB_MUSIC_PLAYER;

public class BackgroundServiceFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = BackgroundServiceFragment.class.getSimpleName();

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
        Button btn_start_job = view.findViewById(R.id.btn_start_job);
        btn_start_job.setOnClickListener(this);
        Button btn_stop_job = view.findViewById(R.id.btn_stop_job);
        btn_stop_job.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            btn_start_service.setVisibility(View.GONE);
            btn_stop_service.setVisibility(View.GONE);
            btn_start_job.setVisibility(View.VISIBLE);
            btn_stop_job.setVisibility(View.VISIBLE);
        } else {
            btn_start_service.setVisibility(View.VISIBLE);
            btn_stop_service.setVisibility(View.VISIBLE);
            btn_start_job.setVisibility(View.GONE);
            btn_stop_job.setVisibility(View.GONE);
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
                        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(getActivity().JOB_SCHEDULER_SERVICE);
                        ComponentName componentName = new ComponentName(getActivity(), MusicPlayerJobScheduler.class);
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
