package com.catherine.materialdesignapp.fragments;

import androidx.fragment.app.Fragment;

import com.catherine.materialdesignapp.listeners.FragmentLifecycle;

public abstract class ChildOfMusicFragment extends Fragment implements FragmentLifecycle {

    public abstract void onFragmentShow();

    public abstract void onFragmentHide();
}
