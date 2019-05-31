package com.catherine.materialdesignapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.catherine.materialdesignapp.FirebaseDB;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.AddSongsAdapter;
import com.catherine.materialdesignapp.components.PlaylistHelper;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.entities.Song;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.listeners.PlaylistHelperListener;
import com.catherine.materialdesignapp.providers.SearchSuggestionProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SearchableSongsActivity extends BaseActivity implements SearchView.OnQueryTextListener, PlaylistHelperListener {
    private final static String TAG = SearchableSongsActivity.class.getSimpleName();
    private SearchManager searchManager;
    private SearchView searchView;
    private Playlist playlist;
    private Map<String, Song> songs;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AddSongsAdapter adapter;
    private PlaylistHelper playlistHelper;
    private boolean sentAddToPlaylistEvent;

    // firebase
    private DatabaseReference myRef;
    private ValueEventListener firebaseValueEventListener;
    private String DB_PATH = FirebaseDB.SONGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_songs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }
        handleIntent(getIntent());
    }

    private void initView() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(DB_PATH);

        swipeRefreshLayout = findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fillInData();
            swipeRefreshLayout.setRefreshing(false);
        });

        RecyclerView recyclerView = findViewById(R.id.rv_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        songs = new HashMap<>();
        adapter = new AddSongsAdapter(this, songs, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClicked:" + adapter.getSongName(position));
                sentAddToPlaylistEvent = true;
                playlistHelper.addToPlaylist(playlist, adapter.getSongName(position), adapter.getSong(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        playlistHelper = new PlaylistHelper(this, this);
        playlistHelper.prepare();
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
        Log.d(TAG, "onNewIntent");
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
            searchView.setOnQueryTextListener(this);
            searchView.setQuery(query, false);
            searchView.clearFocus();
            initView();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
            suggestions.clearHistory();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false; // Start activity with "SEARCH_ACTION" intent-filter
    }

    /**
     * @param newText
     * @return return true to hide search suggestions.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            // TODO: reset data
        }
        return true;
    }

    private void query(String text) {
        // do something
        Log.d(TAG, "query");
        fillInData();
    }

    private void saveQueries(String text) {
        Log.d(TAG, "save: " + text);
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(text, null);
    }


    private void fillInData() {
        if (firebaseValueEventListener != null)
            myRef.removeEventListener(firebaseValueEventListener);

        firebaseValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                songs.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Song song = child.getValue(Song.class);
                    Log.i(TAG, String.format("%s: %s", child.getKey(), song));
                    songs.put(child.getKey(), song);
                }
                adapter.setEntities(songs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                adapter.notifyDataSetChanged();
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
    }

    @Override
    protected void onDestroy() {
        if (firebaseValueEventListener != null)
            myRef.removeEventListener(firebaseValueEventListener);
        super.onDestroy();
    }

    @Override
    public void onDataChanged() {
        if (sentAddToPlaylistEvent) {
            sentAddToPlaylistEvent = false;
            finish();
        }
    }

    @Override
    public void onCancelled() {
        if (sentAddToPlaylistEvent) {
            showSnackbar(swipeRefreshLayout, "failed");
            sentAddToPlaylistEvent = false;
        }
    }
}
