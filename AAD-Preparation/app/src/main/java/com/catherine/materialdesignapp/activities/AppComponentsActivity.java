package com.catherine.materialdesignapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.TabLayoutActivityFSPAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class AppComponentsActivity extends BaseActivity {
    private final static String TAG = AppComponentsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayoutActivityFSPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_components);
        initComponent();
    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
        ViewPager viewpager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        adapter = new TabLayoutActivityFSPAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //left<0.5, right>0.5
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, String.format("onPageSelected:%d", position));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportActionBar().setTitle(adapter.getPageTitle(tab.getPosition()));
                toolbar.setTitle(adapter.getPageTitle(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        getSupportActionBar().setTitle(adapter.getPageTitle(viewpager.getCurrentItem()));
        toolbar.setTitle(adapter.getPageTitle(viewpager.getCurrentItem()));
    }
}
