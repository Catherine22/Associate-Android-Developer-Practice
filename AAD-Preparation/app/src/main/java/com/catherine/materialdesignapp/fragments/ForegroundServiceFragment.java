package com.catherine.materialdesignapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.services.MusicPlayerService;

public class ForegroundServiceFragment extends Fragment {
    public final static String TAG = ForegroundServiceFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foreground_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btn_start_service = view.findViewById(R.id.btn_start_service);
        btn_start_service.setOnClickListener(this::onClick);
        Button btn_stop_service = view.findViewById(R.id.btn_stop_service);
        btn_stop_service.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_service:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent = new Intent(getActivity(), MusicPlayerService.class);
                    getActivity().startForegroundService(intent);
                }
                break;
            case R.id.btn_stop_service:
                Intent intent = new Intent(getActivity(), MusicPlayerService.class);
                getActivity().stopService(intent);
                break;
        }
    }
}
