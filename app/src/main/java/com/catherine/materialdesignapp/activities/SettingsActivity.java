package com.catherine.materialdesignapp.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.catherine.materialdesignapp.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area

        // TODO: add recyclerView or listView, more features like dark mode, localisation supports, font switches and so forth
    }
}
