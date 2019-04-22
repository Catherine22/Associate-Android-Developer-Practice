package com.catherine.materialdesignapp.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.FavoritesFragment;
import com.catherine.materialdesignapp.fragments.HomeFragment;
import com.catherine.materialdesignapp.fragments.MusicFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UIComponentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public final static String TAG = UIComponentsActivity.class.getSimpleName();
    private final String TAG_HOME = "HOME";
    private final String TAG_MUSIC = "MUSIC";
    private final String TAG_FAVORITES = "FAVORITES";

    private Fragment homeFragment, musicFragment, favoritesFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_components);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }

        initView();
    }

    private void initView() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        Menu menu = navigationView.getMenu();
        String[] titles = getResources().getStringArray(R.array.ui_component_bottom_navigation);
        for (int i = 0; i < titles.length; i++) {
            menu.getItem(i).setTitle(titles[i]);
        }
        navigationView.setOnNavigationItemSelectedListener(this);

        // initialise home fragment
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.f_container, homeFragment, TAG_HOME).commit();
        currentFragment = homeFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.nav_home:
                fm.beginTransaction().hide(currentFragment).show(homeFragment).commit();
                currentFragment = homeFragment;
                return true;
            case R.id.nav_music:
                if (musicFragment == null)
                    musicFragment = new MusicFragment();

                if (musicFragment.isAdded()) {
                    fm.beginTransaction().hide(currentFragment).show(musicFragment).commit();
                } else {
                    fm.beginTransaction().hide(currentFragment).add(R.id.f_container, musicFragment, TAG_MUSIC).commit();
                }
                currentFragment = musicFragment;
                return true;
            case R.id.nav_favorites:
                if (favoritesFragment == null)
                    favoritesFragment = new FavoritesFragment();

                if (favoritesFragment.isAdded()) {
                    fm.beginTransaction().hide(currentFragment).show(favoritesFragment).commit();
                } else {
                    fm.beginTransaction().hide(currentFragment).add(R.id.f_container, favoritesFragment, TAG_FAVORITES).commit();
                }
                currentFragment = favoritesFragment;
                return true;
        }
        return false;
    }
}
