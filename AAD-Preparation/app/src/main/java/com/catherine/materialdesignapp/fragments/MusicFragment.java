package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.TabLayoutMusicAdapter;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MusicFragment extends Fragment {
    private final static String TAG = MusicFragment.class.getSimpleName();
//    private SwipeRefreshLayout swipeRefreshLayout;
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
        fillInData();
    }

    private void initView(View view) {
        ViewPager viewpager = view.findViewById(R.id.viewpager);


        TabLayoutMusicAdapter adapter = new TabLayoutMusicAdapter(getActivity().getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        listener.getTabLayout().setVisibility(View.VISIBLE);
        listener.getTabLayout().setupWithViewPager(viewpager);
        listener.getTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                listener.getMyActionBar().setTitle(adapter.getPageTitle(tab.getPosition()));
                listener.getToolbar().setTitle(adapter.getPageTitle(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //left<0.5, right>0.5
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, String.format("onPageSelected:%d", position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        listener.getMyActionBar().setTitle(adapter.getPageTitle(viewpager.getCurrentItem()));
        listener.getToolbar().setTitle(adapter.getPageTitle(viewpager.getCurrentItem()));
    }

    private void fillInData() {

    }
}