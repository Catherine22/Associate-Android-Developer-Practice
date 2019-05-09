package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.TabLayoutMusicAdapter;
import com.catherine.materialdesignapp.listeners.FragmentLifecycle;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;

public class MusicFragment extends Fragment {
    private final static String TAG = MusicFragment.class.getSimpleName();
    public final static String STATE_SELECTED_TAB = "STATE_SELECTED_TAB"; // this works while having MusicFragment selected
    private int oldPosition = 0;
    private ViewPager viewpager;
    private MyOnPageChangeListener onPageChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UIComponentsListener listener = (UIComponentsListener) getActivity();

        viewpager = view.findViewById(R.id.viewpager);
        TabLayoutMusicAdapter adapter = new TabLayoutMusicAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        onPageChangeListener = new MyOnPageChangeListener(adapter);
        viewpager.addOnPageChangeListener(onPageChangeListener);
        listener.addViewPagerManager(viewpager, adapter.TABS);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_SELECTED_TAB, viewpager.getCurrentItem());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.e(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null)
            return;

        // Do not call setCurrentItem directly, because onPageSelected() won't be triggered
        // viewpager.setCurrentItem(restoreTab, false);
        oldPosition = savedInstanceState.getInt(STATE_SELECTED_TAB);
        Log.e(TAG, "oldPosition：" + oldPosition);
        viewpager.post(() -> {
            // tap the tab, the state will be: SETTING -> onPageSelected() -> IDLE
            // scroll the tab, the state will be: DRAGGING -> SETTING -> onPageSelected() -> IDLE
            onPageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_SETTLING);
            onPageChangeListener.onPageSelected(oldPosition);
            onPageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_IDLE);
        });
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private TabLayoutMusicAdapter adapter;

        MyOnPageChangeListener(TabLayoutMusicAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //left<0.5, right>0.5
        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, String.format("onPageSelected: %d", position));
            FragmentLifecycle newFragment = (FragmentLifecycle) adapter.getItem(position);
            newFragment.onFragmentShow();

            FragmentLifecycle oldFragment = (FragmentLifecycle) adapter.getItem(oldPosition);
            oldFragment.onFragmentHide();
            oldPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    Log.d(TAG, "idle");
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    Log.d(TAG, "dragging");
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    Log.d(TAG, "settling");
                    break;
            }
        }
    }
}