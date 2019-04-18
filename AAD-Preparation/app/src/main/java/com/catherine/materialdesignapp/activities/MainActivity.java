package com.catherine.materialdesignapp.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.receivers.NotificationReceiver;
import com.catherine.materialdesignapp.utils.LocationHelper;
import com.catherine.materialdesignapp.utils.OccupiedActions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private NotificationReceiver notificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        handleAppLinks();
        registerReceivers();
    }

    private void initComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showSnackbar(drawer, "sent"));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        String[] menuTitles = getResources().getStringArray(R.array.drawer_array);

        int[] ids = {R.id.nav_app_components, R.id.nav_background, R.id.nav_notification, R.id.nav_manage};
        for (int i = 0; i < ids.length; i++) {
            MenuItem nav = menu.findItem(ids[i]);
            nav.setTitle(menuTitles[i]);
        }

        LocationHelper locationHelper = new LocationHelper();
        TextView tv_location = findViewById(R.id.tv_location);
        tv_location.setText(String.format(Locale.US, "Preferred language: %s", locationHelper.getPreferredLanguage()));
    }

    private void handleAppLinks() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_lifecycle:
                Intent lifecycleIntent = new Intent(this, LifecycleActivity.class);
                startActivity(lifecycleIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_app_components:
                Intent cameraIntent = new Intent(this, AppComponentsActivity.class);
                startActivityForResult(cameraIntent, 12345);
                break;
            case R.id.nav_background:
                Intent backgroundIntent = new Intent(this, BackgroundActivity.class);
                startActivity(backgroundIntent);
                break;
            case R.id.nav_notification:
                Intent notificationActivity = new Intent(this, NotificationActivity.class);
                startActivity(notificationActivity);
                break;
            case R.id.nav_manage:
                Intent aboutIntent = new Intent(this, SettingsActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.nav_share:
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
}
