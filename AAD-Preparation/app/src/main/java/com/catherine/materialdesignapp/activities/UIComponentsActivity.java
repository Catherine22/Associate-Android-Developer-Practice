package com.catherine.materialdesignapp.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.FavoritesFragment;
import com.catherine.materialdesignapp.fragments.HomeFragment;
import com.catherine.materialdesignapp.fragments.MusicFragment;
import com.catherine.materialdesignapp.fragments.PlaylistFragment;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class UIComponentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, UIComponentsListener {
    public final static String TAG = UIComponentsActivity.class.getSimpleName();
    private final String TAG_HOME = "HOME";
    private final String TAG_MUSIC = "MUSIC";
    private final String TAG_FAVORITES = "FAVORITES";

    private Fragment homeFragment, musicFragment, favoritesFragment;
    private Fragment currentFragment;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_components);
        initComponent();
    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        Menu menu = navigationView.getMenu();
        titles = getResources().getStringArray(R.array.ui_component_bottom_navigation);
        for (int i = 0; i < titles.length; i++) {
            menu.getItem(i).setTitle(titles[i]);
        }
        navigationView.setOnNavigationItemSelectedListener(this);

        // initialise home fragment
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.f_container, homeFragment, TAG_HOME).commit();
        currentFragment = homeFragment;


        // ViewPager for MusicFragment
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().hide(currentFragment).show(homeFragment).commit();
                currentFragment = homeFragment;
                toolbar.setTitle(titles[0]);
                tabLayout.setVisibility(View.GONE);
                return true;
            case R.id.nav_music:
                if (musicFragment == null)
                    musicFragment = new MusicFragment();

                if (musicFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).show(musicFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).add(R.id.f_container, musicFragment, TAG_MUSIC).commit();
                }
                currentFragment = musicFragment;
                tabLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.nav_favorite:
                if (favoritesFragment == null)
                    favoritesFragment = new FavoritesFragment();

                if (favoritesFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).show(favoritesFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).add(R.id.f_container, favoritesFragment, TAG_FAVORITES).commit();
                }
                currentFragment = favoritesFragment;
                toolbar.setTitle(titles[2]);
                tabLayout.setVisibility(View.GONE);
                return true;
        }
        return false;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public ActionBar getMyActionBar() {
        return getSupportActionBar();
    }
}
