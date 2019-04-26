package com.catherine.materialdesignapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.FavoritesFragment;
import com.catherine.materialdesignapp.fragments.HomeFragment;
import com.catherine.materialdesignapp.fragments.MusicFragment;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class UIComponentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, UIComponentsListener {
    public final static String TAG = UIComponentsActivity.class.getSimpleName();
    private final String TAG_HOME = "HOME";
    private final String TAG_MUSIC = "MUSIC";
    private final String TAG_FAVORITES = "FAVORITES";

    private BottomNavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private String[] titles;
    private int currentTab;

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

        navigationView = findViewById(R.id.bottom_navigation);
        Menu menu = navigationView.getMenu();
        titles = getResources().getStringArray(R.array.ui_component_bottom_navigation);
        for (int i = 0; i < titles.length; i++) {
            menu.getItem(i).setTitle(titles[i]);
        }
        navigationView.setOnNavigationItemSelectedListener(this);


        // ViewPager for MusicFragment
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);

        // initialise home fragment
        currentTab = R.id.nav_music;
        navigationView.setSelectedItemId(currentTab);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return switchTab(item.getItemId());
    }

    private boolean switchTab(int menuItemId) {
        currentTab = menuItemId;
        switch (menuItemId) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.f_container, new HomeFragment(), TAG_HOME).commit();
                toolbar.setTitle(titles[0]);
                tabLayout.setVisibility(View.GONE);
                return true;
            case R.id.nav_music:
                getSupportFragmentManager().beginTransaction().replace(R.id.f_container, new MusicFragment(), TAG_MUSIC).commit();
                tabLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.nav_favorite:
                getSupportFragmentManager().beginTransaction().replace(R.id.f_container, new FavoritesFragment(), TAG_FAVORITES).commit();
                toolbar.setTitle(titles[2]);
                tabLayout.setVisibility(View.GONE);
                return true;
        }
        currentTab = R.id.nav_home;
        return false;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        navigationView.setSelectedItemId(currentTab);
//    }

    @Override
    public void addViewPagerManager(ViewPager viewpager, String[] titles) {
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportActionBar().setTitle(titles[tab.getPosition()]);
                toolbar.setTitle(titles[tab.getPosition()]);
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
        getSupportActionBar().setTitle(titles[0]);
        toolbar.setTitle(titles[0]);
    }
}
