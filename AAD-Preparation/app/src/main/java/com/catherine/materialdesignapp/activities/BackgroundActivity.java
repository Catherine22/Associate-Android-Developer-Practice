package com.catherine.materialdesignapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.CardRVAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.CardItem;
import com.catherine.materialdesignapp.tasks.SleepTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BackgroundActivity extends BaseActivity {
    public final static String TAG = BackgroundActivity.class.getSimpleName();
    private final static String TEXT_STATE = "currentText";
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardRVAdapter adapter;
    private List<CardItem> cards;
    private String[] titles;
    private String[] subtitles;
    private TextView[] persistentTextViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
        initComponent();
        fillInData(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of the TextView
        for (int i = 0; i < titles.length; i++) {
            if (persistentTextViews[i] != null)
                subtitles[i] = persistentTextViews[i].getText().toString();
        }
        outState.putStringArray(TEXT_STATE, subtitles);
    }


    private void fillInData(Bundle savedInstanceState) {
        cards = new ArrayList<>();
        titles = getResources().getStringArray(R.array.background_task_array);
        subtitles = getResources().getStringArray(R.array.background_task_info_array);

        // restore data
        persistentTextViews = new TextView[titles.length];
        if (savedInstanceState != null) {
            subtitles = savedInstanceState.getStringArray(TEXT_STATE);
        }
        for (int i = 0; i < titles.length; i++) {
            cards.add(new CardItem("https://lh5.googleusercontent.com/-GoUQVw1fnFw/URquv6xbC0I/AAAAAAAAAbs/zEUVTQQ43Zc/s1024/Kauai.jpg", titles[i], subtitles[i], null, null));
        }
        adapter.setEntities(cards);
        adapter.notifyDataSetChanged();
    }

    private void initComponent() {
        swipeRefreshLayout = findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fillInData(null);
            swipeRefreshLayout.setRefreshing(false);
        });
        RecyclerView recyclerView = findViewById(R.id.rv_features);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardRVAdapter(this, cards, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, String.format(Locale.US, "clicked %d", position));
                switch (position) {
                    case 0:
                        TextView subtitle = view.findViewById(R.id.tv_subtitle);
                        persistentTextViews[0] = subtitle;
                        new SleepTask(subtitle).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Hello,", "there!");
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
