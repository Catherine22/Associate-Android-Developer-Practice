package com.trendmicro.diamond.bbc_news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

public class NewsPageActivity extends AppCompatActivity {
    private final static String TAG = NewsPageActivity.class.getSimpleName();
    private final static String BBC_NEWS_URL = "https://www.bbc.com/news";
    private final static int CUSTOM_TABS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(getResources().getColor(R.color.colorPrimary))
                .build();
        customTabsIntent.intent.setData(Uri.parse(BBC_NEWS_URL));
        startActivityForResult(customTabsIntent.intent, CUSTOM_TABS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_TABS_REQUEST_CODE) {
            finish();
        }
    }
}
