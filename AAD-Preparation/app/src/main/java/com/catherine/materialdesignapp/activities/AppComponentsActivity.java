package com.catherine.materialdesignapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.TabLayoutAppComponentsAdapter;
import com.catherine.materialdesignapp.fragments.SelectorFragment;
import com.catherine.materialdesignapp.listeners.ContentProviderFragmentListener;
import com.google.android.material.tabs.TabLayout;

public class AppComponentsActivity extends BaseActivity implements ContentProviderFragmentListener {
    public final static String TAG = AppComponentsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayoutAppComponentsAdapter adapter;
    private ViewPager viewpager;
    private FrameLayout f_container;

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
        f_container = findViewById(R.id.f_container);
        viewpager = findViewById(R.id.viewpager);
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
                Log.d(TAG, "onTabSelected:" + tab.getPosition());
                switchTo(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            String action = intent.getAction();
            int pageId = Integer.parseInt(action);
            viewpager.setCurrentItem(adapter.getPosition(pageId));
        }
        switchTo(viewpager.getCurrentItem());
    }

    private void switchTo(int pos) {
        getSupportActionBar().setTitle(adapter.getPageTitle(pos));
        toolbar.setTitle(adapter.getPageTitle(pos));
    }

    @Override
    public String[] getList(int index) {
        return getResources().getStringArray(R.array.providers);
    }

    @Override
    public void popUpFragment(int index) {
        Fragment f;
        String title = SelectorFragment.TAG;
        Bundle bundle = new Bundle();

        f = new SelectorFragment();
        bundle.putInt("index", index);
        f.setArguments(bundle);

        viewpager.setVisibility(View.INVISIBLE);
        f_container.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(title)
                .replace(R.id.f_container, f, title)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
            viewpager.setVisibility(View.VISIBLE);
            f_container.setVisibility(View.INVISIBLE);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
