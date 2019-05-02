package com.catherine.materialdesignapp.adapters;

import android.os.Parcelable;

import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.AlbumsFragment;
import com.catherine.materialdesignapp.fragments.ArtistsFragment;
import com.catherine.materialdesignapp.fragments.PlaylistFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabLayoutMusicAdapter extends FragmentStatePagerAdapter {
    public String[] TABS = MyApplication.INSTANCE.getResources().getStringArray(R.array.music_array);
    public Fragment[] fragments = new Fragment[3];

    public TabLayoutMusicAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = new AlbumsFragment();
                    break;
                case 1:
                    fragments[position] = new ArtistsFragment();
                    break;
                case 2:
                    fragments[position] = new PlaylistFragment();
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}
