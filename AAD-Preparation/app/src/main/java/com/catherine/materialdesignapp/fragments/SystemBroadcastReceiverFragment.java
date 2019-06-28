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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.ReceiverAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.ReceiverItem;
import com.catherine.materialdesignapp.services.*;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

// Check available system receivers here: https://chromium.googlesource.com/android_tools/+/refs/heads/master/sdk/platforms/android-28/data/broadcast_actions.txt
public class SystemBroadcastReceiverFragment extends Fragment {
    public final static String TAG = SystemBroadcastReceiverFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_system_broadcast_receiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<ReceiverItem> items = new LinkedList<>();
        String[] availableReceivers = getResources().getStringArray(R.array.system_receivers);
        String register = getString(R.string.register_receiver);
        String unregister = getString(R.string.unregister_receiver);
        for (String s : availableReceivers) {
            items.add(new ReceiverItem(s, null, register, unregister));
        }

        RecyclerView rv = view.findViewById(R.id.rv_receivers);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        ReceiverAdapter adapter = new ReceiverAdapter(getActivity(), items, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    scheduleJob(position);
                } else {
                    startService(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    cancelJob(position);
                } else {
                    stopService(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(adapter);
    }

    private void startService(int position) {
        Intent intent = stopService(position);
        getActivity().startService(intent);
    }

    private Intent stopService(int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(getActivity(), ScreenOnOffService.class);
                break;
            case 1:
                intent = new Intent(getActivity(), AirplaneModeService.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        getActivity().stopService(intent);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void scheduleJob(int position) {
        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName;
        int id = cancelJob(position);
        switch (position) {
            case 0:
                componentName = new ComponentName(getActivity(), ScreenOnOffJobScheduler.class);
                break;
            case 1:
                componentName = new ComponentName(getActivity(), AirplaneModeJobScheduler.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }

        try {
            JobInfo jobInfo = new JobInfo.Builder(id, componentName)
                    .setRequiresStorageNotLow(false)
                    .setRequiresBatteryNotLow(true)
                    .setRequiresCharging(false)
                    .build();
            jobScheduler.schedule(jobInfo);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int cancelJob(int position) {
        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(JOB_SCHEDULER_SERVICE);
        int id;
        switch (position) {
            case 0:
                id = BusyJobs.JOB_SCREEN_ON_OFF;
                break;
            case 1:
                id = BusyJobs.JOB_AIRPLANE_MODE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }

        try {
            jobScheduler.cancel(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return id;
    }


}
