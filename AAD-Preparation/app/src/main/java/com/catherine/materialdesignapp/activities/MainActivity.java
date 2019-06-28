package com.catherine.materialdesignapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.TabLayoutAppComponentsAdapter;
import com.catherine.materialdesignapp.fragments.MainFragment;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;
import com.catherine.materialdesignapp.receivers.NotificationReceiver;
import com.catherine.materialdesignapp.utils.OccupiedActions;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Stack;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public final static String TAG = MainActivity.class.getSimpleName();
    private NotificationReceiver notificationReceiver;
    private DrawerLayout drawer;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askForPermission();
        handleAppLinks(getIntent());
    }

    private void askForPermission() {
        getPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new OnRequestPermissionsListener() {
            @Override
            public void onGranted() {
                Log.d(TAG, "onGranted");
                MyApplication.INSTANCE.init(true);
                init();
            }

            @Override
            public void onDenied(@Nullable List<String> deniedPermissions) {
                Log.d(TAG, "onDenied:" + deniedPermissions);
                MyApplication.INSTANCE.init(false);
                init();
            }

            @Override
            public void onRetry() {
                Log.d(TAG, "onRetry");
                askForPermission();
            }
        });
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        String[] menuTitles = getResources().getStringArray(R.array.drawer_array);

        int[] ids = {R.id.nav_app_components, R.id.nav_background, R.id.nav_notification, R.id.nav_ui_components, R.id.nav_lifecycle, R.id.nav_dynamic_delivery};
        for (int i = 0; i < ids.length; i++) {
            MenuItem nav = menu.findItem(ids[i]);
            nav.setTitle(menuTitles[i]);
        }

        fragments = new Fragment[1]; // refer to how many items in Content
        titles = getResources().getStringArray(R.array.main_fragment_titles);
        titleStack = new Stack<>();
        popUpFragment(Content.Default);
        registerReceivers();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        askForPermission();
        handleAppLinks(intent);
    }

    private void handleAppLinks(Intent appLinkIntent) {
        // ATTENTION: This was auto-generated to handle app links.
        Log.d(TAG, "handleAppLinks");
        if (appLinkIntent == null)
            return;
        Log.d(TAG, appLinkIntent.toString());
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        // deal with user-defined actions from receiver
        if (TextUtils.isEmpty(appLinkAction))
            return;


        if (Intent.ACTION_SCREEN_ON.equals(appLinkAction)
                || Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(appLinkAction)
                || Intent.ACTION_BATTERY_LOW.equals(appLinkAction)) {
            Intent appComponentsIntent = new Intent(this, AppComponentsActivity.class);
            appComponentsIntent.setAction(TabLayoutAppComponentsAdapter.BROADCAST_RECEIVER + "");
            startActivityForResult(appComponentsIntent, 12345);
        }
    }

    private void registerReceivers() {
        notificationReceiver = new NotificationReceiver();
        MyApplication.INSTANCE.registerReceiver(notificationReceiver, new IntentFilter(OccupiedActions.ACTION_UPDATE_NOTIFICATION));
    }

    private void unregisterReceivers() {
        try {
            MyApplication.INSTANCE.unregisterReceiver(notificationReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
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

    private void popUpFragment(Content content) {
        String title = titles[content.index];
        Fragment f = null;
        if (content == Content.Default) {
            if (fragments[content.index] == null) {
                f = new MainFragment();
                fragments[content.index] = f;
            } else {
                f = fragments[content.index];
            }
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
            case R.id.nav_ui_components:
                Intent uiComponentsIntent = new Intent(this, UIComponentsActivity.class);
                startActivity(uiComponentsIntent);
                break;
            case R.id.nav_lifecycle:
                Intent lifecycleIntent = new Intent(this, LifecycleActivity.class);
                startActivity(lifecycleIntent);
                break;
            case R.id.nav_dynamic_delivery:
                Intent dynamicDeliveryIntent = new Intent(this, DynamicDeliveryActivity.class);
                startActivity(dynamicDeliveryIntent);
                break;
            default:
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceivers();
        super.onDestroy();
    }
}
