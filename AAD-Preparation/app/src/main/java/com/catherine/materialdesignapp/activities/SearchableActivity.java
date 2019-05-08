package com.catherine.materialdesignapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.models.Playlist;
import com.catherine.materialdesignapp.providers.SearchSuggestionProvider;

public class SearchableActivity extends BaseActivity /*implements SearchView.OnQueryTextListener*/ {
    private final static String TAG = SearchableActivity.class.getSimpleName();
    private SearchManager searchManager;
    private SearchView searchView;
    private Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }
        handleIntent(getIntent());
    }

    /**
     * In this case, this onNewIntent will be called while
     * user finishes searching, this activity will be relaunch.
     * <p>
     * Because
     * 1. this activity launches in single top/task/instance mode
     * 2. ACTION_SEARCH is defined in intent-filter
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        if (intent == null)
            return;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            // Handle the scenario that user submitted searches:
            // 1. Fill in what user just typed in SearchView automatically
            // 2. Dismiss search suggestions
            // 3. Query
            // 4. Save queries
            searchView.setQuery(query, false);
            searchView.clearFocus();
            query(query);
            saveQueries(query);
        }

        if (intent.getParcelableExtra("playlist") != null) {
            playlist = intent.getParcelableExtra("playlist");
            Log.d(TAG, playlist.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchable_menu, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
                suggestions.clearHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false; // Start activity with "SEARCH_ACTION" intent-filter
//    }
//
//    /**
//     * @param newText
//     * @return return true to hide search suggestions.
//     */
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        return false;
//    }

    private void query(String text) {
        // do something
    }

    private void saveQueries(String text) {
        Log.d(TAG, "save: " + text);
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(text, null);
    }
}
