package com.catherine.materialdesignapp.adapters;

import android.os.Build;

import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.BackgroundServiceFragment;
import com.catherine.materialdesignapp.fragments.BroadcastReceiverFragment;
import com.catherine.materialdesignapp.fragments.ContentProviderFragment;
import com.catherine.materialdesignapp.fragments.ForegroundServiceFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabLayoutActivityFSPAdapter extends FragmentStatePagerAdapter {
    private String TABS[];

    public TabLayoutActivityFSPAdapter(FragmentManager fm) {
        super(fm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TABS = MyApplication.INSTANCE.getResources().getStringArray(R.array.app_component_array_O);
        } else {
            TABS = MyApplication.INSTANCE.getResources().getStringArray(R.array.app_component_array);
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
                default:
                    return null;

            }
        } else {
            switch (position) {
                case 0:
                    return new BackgroundServiceFragment();
                case 1:
                    return new BroadcastReceiverFragment();
                case 2:
                    return new ContentProviderFragment();
                default:
                    return null;

            }
        }
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}
