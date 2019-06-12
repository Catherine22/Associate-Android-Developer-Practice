package com.catherine.materialdesignapp.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class BackgroundActivity extends BaseActivity {
    public final static String TAG = BackgroundActivity.class.getSimpleName();
    private final static String TEXT_STATE = "currentText";
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardRVAdapter adapter;
    private List<CardItem> cards;
    private String[] titles;
    private String[] subtitles;
    private TextView[] persistentTextViews;

    private AsyncTask sleepTask1, sleepTask2;
    private Loader sleepTaskLoader1, sleepTaskLoader2;


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
        if (sleepTask1 != null && !sleepTask1.isCancelled()) {
            sleepTask1.cancel(true);
        }
        if (sleepTask2 != null && !sleepTask2.isCancelled()) {
            sleepTask2.cancel(true);
        }
        if (sleepTaskLoader1 != null && sleepTaskLoader1.isStarted()) {
            sleepTaskLoader1.stopLoading();
        }
        if (sleepTaskLoader2 != null && sleepTaskLoader2.isStarted()) {
            sleepTaskLoader2.stopLoading();
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
                        if (sleepTask1 != null && !sleepTask1.isCancelled()) {
                            sleepTask1.cancel(true);
                        } else {
                            sleepTask1 = new SleepTask(subtitle).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Hello,", "there!");
                        }
                        break;
                    case 1:
                        // AsyncTask
                        persistentTextViews[1] = subtitle;
                        if (sleepTask2 != null && !sleepTask2.isCancelled()) {
                            sleepTask2.cancel(true);
                        } else {
                            sleepTask2 = new SleepTask(subtitle).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Hello,", "there!");
                        }
                        break;
                    case 2:
                        // AsyncTaskLoader
                        persistentTextViews[2] = subtitle;
                        Bundle b1 = new Bundle();
                        b1.putString("message", "Hello, there!");
                        if (sleepTaskLoader1 == null) {
                            sleepTaskLoader1 = getLoaderManager().initLoader(LoaderIds.SLEEP_TASK1.getValue(), b1, new LoaderCallbacksImpl(2));
                        } else {
                            if (sleepTaskLoader1.isStarted())
                                sleepTaskLoader1.stopLoading();
                            else
                                getLoaderManager().restartLoader(LoaderIds.SLEEP_TASK1.getValue(), b1, new LoaderCallbacksImpl(2));
                        }
                        break;
                    case 3:
                        // AsyncTaskLoader
                        persistentTextViews[3] = subtitle;
                        Bundle b2 = new Bundle();
                        b2.putString("message", "Hello, there!");
                        if (sleepTaskLoader2 == null) {
                            sleepTaskLoader2 = getLoaderManager().initLoader(LoaderIds.SLEEP_TASK2.getValue(), b2, new LoaderCallbacksImpl(3));
                        } else {
                            if (sleepTaskLoader2.isStarted())
                                sleepTaskLoader2.stopLoading();
                            else
                                getLoaderManager().restartLoader(LoaderIds.SLEEP_TASK2.getValue(), b2, new LoaderCallbacksImpl(3));
                        }
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
        private int index;

        public LoaderCallbacksImpl(int index) {
            this.index = index;
        }

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
                if (persistentTextViews != null && persistentTextViews[index] != null)
                    persistentTextViews[index].setText(data);
            });
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            // the created loader is being reset
            Log.i(TAG, "onLoaderReset");
            runOnUiThread(() -> {
                if (persistentTextViews != null && persistentTextViews[index] != null)
                    persistentTextViews[index].setText(subtitles[index]);
            });
        }
    }

    @Override
    protected void onDestroy() {
        getLoaderManager().destroyLoader(LoaderIds.SLEEP_TASK1.getValue());
        getLoaderManager().destroyLoader(LoaderIds.SLEEP_TASK2.getValue());
        super.onDestroy();
    }
}
