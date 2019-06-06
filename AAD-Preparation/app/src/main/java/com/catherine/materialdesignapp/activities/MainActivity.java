package com.catherine.materialdesignapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.fragments.MainFragment;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;
import com.catherine.materialdesignapp.receivers.NotificationReceiver;
import com.catherine.materialdesignapp.utils.OccupiedActions;
import com.catherine.materialdesignapp.utils.Storage;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Stack;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnRequestPermissionsListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private NotificationReceiver notificationReceiver;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Fragment[] fragments;
    private String[] titles;
    private Stack<String> titleStack;

    private enum Content {
        Default(0);

        private int index;

        Content(int index) {
            this.index = index;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        handleAppLinks(getIntent());
        registerReceivers();
        getPermissions(permissions, this);
    }

    // We set the theme, immediately in the Activity’s onCreate()
    private void initNightMode() {
        Storage storage = new Storage(this);
        int nightMode = storage.retrieveInt(Storage.NIGHT_MODE);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void initComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        String[] menuTitles = getResources().getStringArray(R.array.drawer_array);

        int[] ids = {R.id.nav_app_components, R.id.nav_background, R.id.nav_notification, R.id.nav_manage, R.id.nav_lifecycle};
        for (int i = 0; i < ids.length; i++) {
            MenuItem nav = menu.findItem(ids[i]);
            nav.setTitle(menuTitles[i]);
        }

        fragments = new Fragment[1]; // refer to how many items in Content
        titles = getResources().getStringArray(R.array.main_fragment_titles);
        titleStack = new Stack<>();
        popUpFragment(Content.Default);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleAppLinks(intent);
    }

    private void handleAppLinks(Intent appLinkIntent) {
        // ATTENTION: This was auto-generated to handle app links.
        if (appLinkIntent == null)
            return;
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void registerReceivers() {
        notificationReceiver = new NotificationReceiver();
        registerReceiver(notificationReceiver, new IntentFilter(OccupiedActions.ACTION_UPDATE_NOTIFICATION));
    }

    private void unregisterReceivers() {
        unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                finish();
            else {
                super.onBackPressed();
                titleStack.pop();
                getSupportActionBar().setTitle(titleStack.peek());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        Storage storage = new Storage(this);
        int nightMode = storage.retrieveInt(Storage.NIGHT_MODE);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.getItem(0).setTitle(getString(R.string.action_day_mode));
        } else {
            menu.getItem(0).setTitle(getString(R.string.action_night_mode));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_night_mode:
                Storage storage = new Storage(this);
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    storage.save(Storage.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    item.setTitle(getString(R.string.action_day_mode));
                    storage.save(Storage.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_YES);
                }
                // Recreate the activity for the theme change to take effect.
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void popUpFragment(Content content) {
        String title = titles[content.index];
        Fragment f = null;
        switch (content) {
            case Default:
                if (fragments[content.index] == null) {
                    f = new MainFragment();
                    fragments[content.index] = f;
                } else {
                    f = fragments[content.index];
                }
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(title)
                .replace(R.id.f_container, f, title)
                .commit();
        titleStack.push(title);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_app_components:
                Intent appComponentsIntent = new Intent(this, AppComponentsActivity.class);
                startActivityForResult(appComponentsIntent, 12345);
                break;
            case R.id.nav_background:
                Intent backgroundIntent = new Intent(this, BackgroundActivity.class);
                startActivity(backgroundIntent);
                break;
            case R.id.nav_notification:
                Intent notificationIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificationIntent);
                break;
            case R.id.nav_manage:
                Intent uiComponentsIntent = new Intent(this, UIComponentsActivity.class);
                startActivity(uiComponentsIntent);
                break;
            case R.id.nav_lifecycle:
                Intent lifecycleIntent = new Intent(this, LifecycleActivity.class);
                startActivity(lifecycleIntent);
                break;
            case R.id.nav_send:
                break;
            default:
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceivers();
        super.onDestroy();
    }

    @Override
    public void onGranted() {
        MyApplication.INSTANCE.init(true);
    }

    @Override
    public void onDenied(@Nullable List<String> deniedPermissions) {
        MyApplication.INSTANCE.init(false);
    }

    @Override
    public void onRetry() {
        getPermissions(permissions, this);
    }
}
