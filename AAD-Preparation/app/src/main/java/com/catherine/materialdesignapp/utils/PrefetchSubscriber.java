package com.catherine.materialdesignapp.utils;

import android.util.Log;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;

public class PrefetchSubscriber extends BaseDataSubscriber<Void> {
    public final static String TAG = PrefetchSubscriber.class.getSimpleName();
    private int succeed, failed;

    @Override
    protected void onNewResultImpl(DataSource<Void> dataSource) {
        ++succeed;
        Log.i(TAG, String.format("Cached: %d", succeed));
    }

    @Override
    protected void onFailureImpl(DataSource<Void> dataSource) {
        ++failed;
        Log.e(TAG, String.format("Failed: %d", failed));
    }
}
