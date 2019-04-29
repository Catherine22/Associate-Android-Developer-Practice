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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class UIComponentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, UIComponentsListener, FragmentManager.OnBackStackChangedListener {
    public final static String TAG = UIComponentsActivity.class.getSimpleName();
    private final String TAG_HOME = "HOME";
    private final String TAG_MUSIC = "MUSIC";
    private final String TAG_FAVORITES = "FAVORITES";

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


        // ViewPager for MusicFragment
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        // initialise home fragment
        navigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return switchTab(item.getItemId());
    }

    private boolean switchTab(int menuItemId) {
        String tag;
        Fragment f;
        switch (menuItemId) {
            case R.id.nav_home:
                tag = TAG_HOME;
                f = new HomeFragment();
                toolbar.setTitle(titles[0]);
                tabLayout.setVisibility(View.GONE);
                break;
            case R.id.nav_music:
                tag = TAG_MUSIC;
                f = new MusicFragment();
                tabLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_favorite:
                tag = TAG_FAVORITES;
                f = new FavoritesFragment();
                toolbar.setTitle(titles[2]);
                tabLayout.setVisibility(View.GONE);
                break;
            default:
                return false;
        }
        Log.e(TAG, String.format("onSwitch:%s", tag));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.f_container, f, tag)
                .commit();
        return true;
    }

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

    @Override
    public void onBackStackChanged() {
        Log.e(TAG, "count:" + getSupportFragmentManager().getBackStackEntryCount());
    }
}
