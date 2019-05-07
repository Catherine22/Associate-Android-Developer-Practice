package com.catherine.materialdesignapp.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.catherine.materialdesignapp.R;

public class SongsActivity extends BaseActivity {
    private final static String TAG = SongsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }
    }
}
