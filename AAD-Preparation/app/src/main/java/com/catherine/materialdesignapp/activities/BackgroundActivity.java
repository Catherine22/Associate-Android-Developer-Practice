package com.catherine.materialdesignapp.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.CardRVAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.CardItem;
import com.catherine.materialdesignapp.tasks.LoaderIds;
import com.catherine.materialdesignapp.tasks.SleepTask;
import com.catherine.materialdesignapp.tasks.SleepTaskLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BackgroundActivity extends BaseActivity {
    public final static String TAG = BackgroundActivity.class.getSimpleName();
    private final static String TEXT_STATE = "currentText";
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardRVAdapter adapter;
    private List<CardItem> cards;
    private String[] titles;
    private String[] subtitles;
    private TextView[] persistentTextViews;

    private AsyncTask sleepTask;
    private Loader sleepTaskLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }
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

        // stop tasks
        if (sleepTask != null && !sleepTask.isCancelled()) {
            sleepTask.cancel(true);
        }
        if (sleepTaskLoader != null && sleepTaskLoader.isStarted()) {
            sleepTaskLoader.stopLoading();
        }

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
                Log.i(TAG, String.format(Locale.US, "clicked %d", position));
                TextView subtitle = view.findViewById(R.id.tv_subtitle);
                switch (position) {
                    case 0:
                        // AsyncTask
                        persistentTextViews[0] = subtitle;
                        if (sleepTask != null && !sleepTask.isCancelled()) {
                            sleepTask.cancel(true);
                        } else {
                            sleepTask = new SleepTask(subtitle).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Hello,", "there!");
                        }
                        break;
                    case 1:
                        // AsyncTaskLoader
                        persistentTextViews[1] = subtitle;
                        Bundle b = new Bundle();
                        b.putString("message", "Hello, there!");
                        if (sleepTaskLoader == null) {
                            sleepTaskLoader = getLoaderManager().initLoader(LoaderIds.SLEEP_TASK.getValue(), b, new LoaderCallbacksImpl());
                        } else {
                            if (sleepTaskLoader.isStarted())
                                sleepTaskLoader.stopLoading();
                            else
                                getLoaderManager().restartLoader(LoaderIds.SLEEP_TASK.getValue(), b, new LoaderCallbacksImpl());
                        }
                        break;
                    case 2:
                        // ViewModels and LiveData
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

    class LoaderCallbacksImpl implements LoaderManager.LoaderCallbacks<String> {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            // this will be called if the id is unique, which has never been used before.
            Log.i(TAG, "onCreateLoader");
            return new SleepTaskLoader(BackgroundActivity.this, args.getString("message"));
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            // finished loading
            Log.i(TAG, "onLoadFinished");
            runOnUiThread(() -> {
                if (persistentTextViews != null && persistentTextViews[1] != null)
                    persistentTextViews[1].setText(data);
            });
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            // the created loader is being reset
            Log.i(TAG, "onLoaderReset");
            runOnUiThread(() -> {
                if (persistentTextViews != null && persistentTextViews[1] != null)
                    persistentTextViews[1].setText(subtitles[1]);
            });
        }
    }

    @Override
    protected void onDestroy() {
        getLoaderManager().destroyLoader(LoaderIds.SLEEP_TASK.getValue());
        super.onDestroy();
    }
}
