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
import com.catherine.materialdesignapp.jetpack.view_models.ArtistViewModel;
import com.catherine.materialdesignapp.jetpack.view_models.ArtistViewModelFactory;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;

public class ArtistsFragment extends ChildOfMusicFragment implements OnSearchViewListener {
    public final static String TAG = ArtistsFragment.class.getSimpleName();
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
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
        RecyclerView recyclerView = view.findViewById(R.id.rv_artist);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), ArtistAdapter.MAX_COLUMNS));

        ArtistAdapter adapter = new ArtistAdapter();
        recyclerView.setAdapter(adapter);
        ArtistItemKeyProvider artistItemKeyProvider = new ArtistItemKeyProvider(null);
        SelectionTracker<String> tracker = new SelectionTracker.Builder<>(
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

        if (getActivity() == null)
            return;

        // RoomDatabase
        ArtistViewModelFactory artistViewModelFactory = ArtistViewModelFactory.createFactory(getActivity());
        artistViewModel = ViewModelProviders.of(this, artistViewModelFactory).get(ArtistViewModel.class);
        artistViewModel.getArtistLiveData().observe(this, artists -> {
            adapter.submitList(artists);
            artistItemKeyProvider.updateList(artists.snapshot());
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        artistViewModel.search(newText);
        return false;
    }

    @Override
    public void onFragmentShow() {
        if (listener != null)
            listener.setOnSearchListener(this);
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
        super.onDestroy();
    }
}
