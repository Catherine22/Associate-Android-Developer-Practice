package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.ArtistAdapter;
import com.catherine.materialdesignapp.components.ArtistItemDetailsLookup;
import com.catherine.materialdesignapp.components.ArtistItemKeyProvider;
import com.catherine.materialdesignapp.jetpack.entities.Artist;
import com.catherine.materialdesignapp.jetpack.view_models.ArtistViewModel;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.catherine.materialdesignapp.utils.TextHelper;

import java.util.ArrayList;
import java.util.List;

public class ArtistsFragment extends ChildOfMusicFragment implements OnSearchViewListener {
    public final static String TAG = ArtistsFragment.class.getSimpleName();
    private ArtistAdapter adapter;
    private List<Artist> artists;
    private List<Artist> filteredArtists;
    private ArtistItemKeyProvider artistItemKeyProvider;
    private SelectionTracker<String> tracker;
    private UIComponentsListener listener;

    private ArtistViewModel artistViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });
        RecyclerView recyclerView = view.findViewById(R.id.rv_artist);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), ArtistAdapter.MAX_COLUMNS));

        artists = new ArrayList<>();
        filteredArtists = new ArrayList<>();
        adapter = new ArtistAdapter(getActivity(), artists);
        recyclerView.setAdapter(adapter);
        artistItemKeyProvider = new ArtistItemKeyProvider(artists);
        tracker = new SelectionTracker.Builder<>(
                "my-selection-id",
                recyclerView,
                artistItemKeyProvider,
                new ArtistItemDetailsLookup(recyclerView),
                StorageStrategy.createStringStorage())
                // for single selection
                // .withSelectionPredicate(SelectionPredicates.<String>createSelectSingleAnything())
                .build();

        adapter.setSelectionTracker(tracker);
        tracker.addObserver(new SelectionObserver());
        listener = (UIComponentsListener) getActivity();

        // RoomDatabase
        artistViewModel = ViewModelProviders.of(this).get(ArtistViewModel.class);
        artistViewModel.getArtistLiveData().observe(this, artists -> {
            filteredArtists.clear();
            filteredArtists.addAll(artists);
            updateList();
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filteredArtists.clear();
        for (Artist artist : artists) {
            if (TextHelper.matcher(artist.getArtist(), newText)) {
                filteredArtists.add(artist);
            }
        }
        updateList();
        return false;
    }

    private void updateList() {
        tracker.clearSelection();
        adapter.setEntities(filteredArtists);
        artistItemKeyProvider.updateList(filteredArtists);
        try {
            getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentShow() {
        if (listener != null)
            listener.addOnSearchListener(this);
    }

    @Override
    public void onFragmentHide() {

    }

    private class SelectionObserver extends SelectionTracker.SelectionObserver<String> {
        @Override
        public void onItemStateChanged(@NonNull String key, boolean selected) {
            Log.d(TAG, String.format("onItemStateChanged: %s, isSelected: %b", key, selected));
            super.onItemStateChanged(key, selected);
        }
    }


    @Override
    public void onDestroy() {
        artistViewModel.release();
        super.onDestroy();
    }
}
