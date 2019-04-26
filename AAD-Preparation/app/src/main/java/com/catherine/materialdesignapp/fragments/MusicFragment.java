package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.TabLayoutMusicAdapter;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MusicFragment extends Fragment {
    private final static String TAG = MusicFragment.class.getSimpleName();
    private UIComponentsListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (UIComponentsListener) getActivity();
        initView(view);
    }

    private void initView(View view) {
        ViewPager viewpager = view.findViewById(R.id.viewpager);
        TabLayoutMusicAdapter adapter = new TabLayoutMusicAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        listener.addViewPagerManager(viewpager, adapter.TABS);
    }
}