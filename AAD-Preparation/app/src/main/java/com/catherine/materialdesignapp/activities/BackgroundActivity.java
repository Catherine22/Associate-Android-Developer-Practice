package com.catherine.materialdesignapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.CardRVAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.CardItem;
import com.catherine.materialdesignapp.tasks.SleepTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BackgroundActivity extends BaseActivity implements OnClickListener {
    public final static String TAG = BackgroundActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardRVAdapter adapter;
    private List<CardItem> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
        initComponent();
        fillInData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void fillInData() {
        cards = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.background_task_array);
        for (int i = 0; i < titles.length; i++) {
            cards.add(new CardItem("https://lh5.googleusercontent.com/-GoUQVw1fnFw/URquv6xbC0I/AAAAAAAAAbs/zEUVTQQ43Zc/s1024/Kauai.jpg", titles[i], String.format(Locale.ENGLISH, "subtitle %d", i), null, null));
        }
        adapter.setEntities(cards);
        adapter.notifyDataSetChanged();
    }

    private void initComponent() {
        swipeRefreshLayout = findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillInData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.rv_features);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardRVAdapter(this, cards, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, String.format(Locale.US, "clicked %d", position));
                TextView subtitle = view.findViewById(R.id.tv_subtitle);
                switch (position) {
                    case 0:
                        new SleepTask(subtitle).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "hello,", "there!");
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        adapter.setOnLeftButtonClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }
}
