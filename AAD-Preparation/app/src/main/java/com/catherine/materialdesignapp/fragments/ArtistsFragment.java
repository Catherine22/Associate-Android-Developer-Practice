package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.ArtistAdapter;
import com.catherine.materialdesignapp.components.ArtistItemDetailsLookup;
import com.catherine.materialdesignapp.components.ArtistItemKeyProvider;
import com.catherine.materialdesignapp.models.Artist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ArtistsFragment extends Fragment {
    private final static String TAG = ArtistsFragment.class.getSimpleName();
    private ArtistAdapter adapter;
    private List<Artist> artists;
    private RecyclerView recyclerView;
    private ArtistItemKeyProvider artistItemKeyProvider;
    private SelectionTracker<String> tracker;

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
            fillInData();
            swipeRefreshLayout.setRefreshing(false);
        });
        recyclerView = view.findViewById(R.id.rv_artist);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), ArtistAdapter.MAX_COLUMNS));

        artists = new ArrayList<>();
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
        fillInData();
    }

    private void fillInData() {
        String mockData = "[\n" +
                "  {\n" +
                "    \"artist\": \"Taylor Swift\",\n" +
                "    \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "    \"image\": \"https://images-na.ssl-images-amazon.com/images/I/71YCfdyMXsL.jpg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"artist\": \"Kanye West\",\n" +
                "    \"url\": \"https://en.wikipedia.org/wiki/Kanye_West\",\n" +
                "    \"image\": \"https://images-na.ssl-images-amazon.com/images/I/71w7GAs8SeL.jpg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"artist\": \"Beyoncé\",\n" +
                "    \"url\": \"https://en.wikipedia.org/wiki/Beyonc%C3%A9\",\n" +
                "    \"image\": \"https://images-na.ssl-images-amazon.com/images/I/51T6RpubiVL.jpg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"artist\": \"Ariana Grande\",\n" +
                "    \"url\": \"https://en.wikipedia.org/wiki/Ariana_Grande\",\n" +
                "    \"image\": \"https://images-na.ssl-images-amazon.com/images/I/7192dgpkzTL.jpg\"\n" +
                "  }\n" +
                "]";

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Artist>>() {
        }.getType();
        artists = gson.fromJson(mockData, listType);
        adapter.setEntities(artists);
        tracker.clearSelection();
        artistItemKeyProvider.updateList(artists);
        adapter.notifyDataSetChanged();
    }

    class SelectionObserver extends SelectionTracker.SelectionObserver<String> {
        @Override
        public void onItemStateChanged(@NonNull String key, boolean selected) {
            Log.d(TAG, String.format("onItemStateChanged: %s, isSelected: %b", key, selected));
            super.onItemStateChanged(key, selected);
        }

        @Override
        public void onSelectionRefresh() {
            super.onSelectionRefresh();
        }

        @Override
        public void onSelectionChanged() {
            super.onSelectionChanged();
        }

        @Override
        public void onSelectionRestored() {
            super.onSelectionRestored();
        }
    }
}
