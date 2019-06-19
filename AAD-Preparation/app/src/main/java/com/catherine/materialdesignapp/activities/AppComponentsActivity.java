package com.catherine.materialdesignapp.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.TabLayoutAppComponentsAdapter;
import com.google.android.material.tabs.TabLayout;

public class AppComponentsActivity extends BaseActivity {
    public final static String TAG = AppComponentsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayoutAppComponentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_components);
        initComponent();
    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }
        ViewPager viewpager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        adapter = new TabLayoutAppComponentsAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
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
