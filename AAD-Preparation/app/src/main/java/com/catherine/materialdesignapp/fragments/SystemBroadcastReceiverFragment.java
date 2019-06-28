package com.catherine.materialdesignapp.fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.MyApplication;
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
    private BatteryLowConnection batteryLowConnection;
    private List<ReceiverItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_system_broadcast_receiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = new LinkedList<>();
        String[] availableReceivers = getResources().getStringArray(R.array.system_receivers);
        String startService = getString(R.string.start_service);
        String stopService = getString(R.string.stop_service);
        String bindService = getString(R.string.bind_service);
        String unbindService = getString(R.string.unbind_service);
        items.add(new ReceiverItem(availableReceivers[0], null, startService, stopService));
        items.add(new ReceiverItem(availableReceivers[1], null, startService, stopService));
        items.add(new ReceiverItem(availableReceivers[2], null, bindService, unbindService));

        RecyclerView rv = view.findViewById(R.id.rv_receivers);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        ReceiverAdapter adapter = new ReceiverAdapter(getActivity(), items, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    scheduleJob(position);
                } else {
                    if (position == 2)
                        bindService(position);
                    else
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
                    if (position == 2)
                        unbindService(position);
                    else
                        stopService(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(adapter);

        // init serviceConnection to bind services
        batteryLowConnection = new BatteryLowConnection();
    }

    private void bindService(int position) {
        Intent intent;
        if (position == 2) {
            intent = new Intent(getActivity(), BatteryLowService.class);
            MyApplication.INSTANCE.bindService(intent, batteryLowConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindService(int position) {
        if (position == 2) {
            if (batteryLowConnection != null)
                MyApplication.INSTANCE.unbindService(batteryLowConnection);
        }

        ReceiverItem newItem = items.get(position);
        newItem.subtitle = "";
        items.set(position, newItem);
    }

    @Override
    public void onDestroy() {
        if (batteryLowConnection != null)
            MyApplication.INSTANCE.unbindService(batteryLowConnection);
        super.onDestroy();
    }

    public class BatteryLowConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    }


    private void startService(int position) {
        Intent intent = stopService(position);
        MyApplication.INSTANCE.startService(intent);
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
        MyApplication.INSTANCE.stopService(intent);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void scheduleJob(int position) {
        JobScheduler jobScheduler = (JobScheduler) MyApplication.INSTANCE.getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName;
        int id = cancelJob(position);
        switch (position) {
            case 0:
                componentName = new ComponentName(MyApplication.INSTANCE, ScreenOnOffJobScheduler.class);
                break;
            case 1:
                componentName = new ComponentName(MyApplication.INSTANCE, AirplaneModeJobScheduler.class);
                break;
            case 2:
                componentName = new ComponentName(MyApplication.INSTANCE, BatteryLowJobScheduler.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }

        JobInfo jobInfo = new JobInfo.Builder(id, componentName)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .build();
        if (jobScheduler != null)
            jobScheduler.schedule(jobInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int cancelJob(int position) {
        JobScheduler jobScheduler = (JobScheduler) MyApplication.INSTANCE.getSystemService(JOB_SCHEDULER_SERVICE);
        int id;
        switch (position) {
            case 0:
                id = BusyJobs.JOB_SCREEN_ON_OFF;
                break;
            case 1:
                id = BusyJobs.JOB_AIRPLANE_MODE;
                break;
            case 2:
                id = BusyJobs.JOB_BATTERY_LOW;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        if (jobScheduler != null)
            jobScheduler.cancel(id);

        return id;
    }


}
