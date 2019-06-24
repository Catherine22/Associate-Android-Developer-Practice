package com.catherine.materialdesignapp.adapters;

import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.BackgroundServiceFragment;
import com.catherine.materialdesignapp.fragments.BroadcastReceiverFragment;
import com.catherine.materialdesignapp.fragments.ContentProviderFragment;
import com.catherine.materialdesignapp.fragments.ForegroundServiceFragment;

public class TabLayoutAppComponentsAdapter extends FragmentStatePagerAdapter {
    private String[] TABS;
    private int[] pageIds;
    public final static int FOREGROUND_SERVICE = 0;
    public final static int BACKGROUND_SERVICE = 1;
    public final static int BROADCAST_RECEIVER = 2;
    public final static int CONTENT_PROVIDER = 3;

    public TabLayoutAppComponentsAdapter(FragmentManager fm) {
        super(fm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TABS = MyApplication.INSTANCE.getResources().getStringArray(R.array.app_component_array_O);
            pageIds = new int[]{
                    FOREGROUND_SERVICE, BACKGROUND_SERVICE, BROADCAST_RECEIVER, CONTENT_PROVIDER
            };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TABS = MyApplication.INSTANCE.getResources().getStringArray(R.array.app_component_array_lollipop);
            pageIds = new int[]{
                    BACKGROUND_SERVICE, BROADCAST_RECEIVER, CONTENT_PROVIDER
            };
        } else {
            TABS = MyApplication.INSTANCE.getResources().getStringArray(R.array.app_component_array);
            pageIds = new int[]{
                    BACKGROUND_SERVICE, BROADCAST_RECEIVER, CONTENT_PROVIDER
            };
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            switch (position) {
                case 0:
                    return new ForegroundServiceFragment();
                case 1:
                    return new BackgroundServiceFragment();
                case 2:
                    return new BroadcastReceiverFragment();
                case 3:
                    return new ContentProviderFragment();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (position) {
                case 0:
                    return new BackgroundServiceFragment();
                case 1:
                    return new BroadcastReceiverFragment();
                case 2:
                    return new ContentProviderFragment();
            }
        } else {
            switch (position) {
                case 0:
                    return new BackgroundServiceFragment();
                case 1:
                    return new BroadcastReceiverFragment();
                case 2:
                    return new ContentProviderFragment();

            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    public int getPageId(int position) {
        return pageIds[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}
