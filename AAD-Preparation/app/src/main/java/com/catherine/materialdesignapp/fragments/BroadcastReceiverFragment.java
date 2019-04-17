package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.R;

public class BroadcastReceiverFragment extends Fragment {
    private final static String TAG = BroadcastReceiverFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_broadcast_receiver, container, false);
    }
}
