package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.R;

import androidx.fragment.app.Fragment;

public class MusicFragment extends Fragment {
    private final static String TAG = MusicFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false);
    }
}