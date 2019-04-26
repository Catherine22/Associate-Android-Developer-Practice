package com.catherine.materialdesignapp.adapters;

import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.AlbumsFragment;
import com.catherine.materialdesignapp.fragments.ArtistsFragment;
import com.catherine.materialdesignapp.fragments.PlaylistFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabLayoutMusicAdapter extends FragmentStatePagerAdapter {
    public String TABS[] = MyApplication.INSTANCE.getResources().getStringArray(R.array.music_array);

    public TabLayoutMusicAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AlbumsFragment();
            case 1:
                return new ArtistsFragment();
            case 2:
                return new PlaylistFragment();
        }
        return null;
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
