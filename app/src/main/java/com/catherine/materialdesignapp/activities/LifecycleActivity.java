package com.catherine.materialdesignapp.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.utils.LifecycleObserverImpl;

public class LifecycleActivity extends BaseActivity implements LifecycleOwner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area

        getLifecycle().addObserver(new LifecycleObserverImpl());
    }
}
