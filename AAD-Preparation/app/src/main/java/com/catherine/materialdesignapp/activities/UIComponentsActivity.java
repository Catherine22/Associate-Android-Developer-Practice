package com.catherine.materialdesignapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.FavoritesFragment;
import com.catherine.materialdesignapp.fragments.HomeFragment;
import com.catherine.materialdesignapp.fragments.MusicFragment;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class UIComponentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        UIComponentsListener, FragmentManager.OnBackStackChangedListener, SearchView.OnQueryTextListener {
    public final static String TAG = UIComponentsActivity.class.getSimpleName();
    private final String TAG_HOME = "HOME";
    private final String TAG_MUSIC = "MUSIC";
    private final String TAG_FAVORITES = "FAVORITES";


    private final static String STATE_SELECTED_BOTTOM_NAVIGATION = "STATE_SELECTED_BOTTOM_NAVIGATION";
    public final static String STATE_SELECTED_TAB = "STATE_SELECTED_TAB"; // this works while having MusicFragment selected
    private int selectedTab = 0; // this works while having MusicFragment selected

    private BottomNavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private String[] titles;
    private Fragment[] fragments = new Fragment[3];
    private OnSearchViewListener onSearchViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_components);
        initComponent();

        handleIntent(getIntent());
    }


    /**
     * If your searchable activity launches in single top mode (android:launchMode="singleTop"),
     * also handle the ACTION_SEARCH intent in the onNewIntent() method
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
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

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        // initialise home fragment
        navigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ui_components_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return switchTab(item.getItemId());
    }

    private boolean switchTab(int menuItemId) {
        String tag;
        Fragment f;
        int index;
        switch (menuItemId) {
            case R.id.nav_home:
                tag = TAG_HOME;
                index = 0;
                if (fragments[index] == null)
                    fragments[index] = new HomeFragment();
                f = fragments[index];
                toolbar.setTitle(titles[index]);
                tabLayout.setVisibility(View.GONE);
                break;
            case R.id.nav_music:
                tag = TAG_MUSIC;
                index = 1;
                if (fragments[index] == null) {
                    fragments[index] = new MusicFragment();

                    Bundle b = new Bundle();
                    b.putInt(STATE_SELECTED_TAB, selectedTab);
                    fragments[index].setArguments(b);
                }
                f = fragments[index];
                tabLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_favorite:
                tag = TAG_FAVORITES;
                index = 2;
                if (fragments[index] == null)
                    fragments[index] = new FavoritesFragment();
                f = fragments[index];
                toolbar.setTitle(titles[index]);
                tabLayout.setVisibility(View.GONE);
                break;
            default:
                return false;
        }
        Log.e(TAG, String.format("onTabSwitch:%s", tag));

        // keep only one fragment in the back stack
        clearBackStack();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.f_container, f, tag)
                .commit();
        return true;
    }

    @Override
    public void addViewPagerManager(ViewPager viewpager, String[] titles) {
        this.viewpager = viewpager;
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
    public void addOnSearchListener(OnSearchViewListener listener) {
        this.onSearchViewListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1)
            finish();
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackStackChanged() {
        Log.w(TAG, String.format("Back stack counts: %d", getSupportFragmentManager().getBackStackEntryCount()));
    }

    private void clearBackStack() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_SELECTED_BOTTOM_NAVIGATION, navigationView.getSelectedItemId());
        savedInstanceState.putInt(STATE_SELECTED_TAB, viewpager.getCurrentItem());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int nav = savedInstanceState.getInt(STATE_SELECTED_BOTTOM_NAVIGATION);
        selectedTab = savedInstanceState.getInt(STATE_SELECTED_TAB);
        navigationView.setSelectedItemId(nav);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (onSearchViewListener != null)
            onSearchViewListener.onQueryTextSubmit(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (onSearchViewListener != null)
            onSearchViewListener.onQueryTextChange(newText);
        return false;
    }
}
