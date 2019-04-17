package com.catherine.materialdesignapp.activities;

import android.os.Bundle;

import com.catherine.materialdesignapp.R;

import androidx.appcompat.widget.Toolbar;

public class NotificationActivity extends BaseActivity {
    public final static String TAG = NotificationActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
        initComponent();
    }

    private void initComponent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
