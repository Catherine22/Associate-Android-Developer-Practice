package com.catherine.materialdesignapp.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.catherine.materialdesignapp.R;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        handleAppLinks();
    }

    private void initComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbar("sent");
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void handleAppLinks() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
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
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
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


    private void showSnackbar(CharSequence message) {
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        final Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
        snackbar.setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                // do something
            }
        });
        snackbar.show();
    }

    private void showBasicToast(CharSequence message) {
        makeBasicToast(message).show();
    }

    private void showToastOnTopLeft(CharSequence message) {
        Toast toast = makeBasicToast(message);
        toast.setGravity(Gravity.TOP | Gravity.START, 100, 50);
        toast.show();
    }

    private void showCustomToast(CharSequence message) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.c_toast, (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView textView = layout.findViewById(R.id.text);
        textView.setText(message);

        Toast toast = new Toast(this);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP | Gravity.START, 100, 50);
        toast.show();
    }

    private Toast makeBasicToast(CharSequence message) {
        return Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }
}
