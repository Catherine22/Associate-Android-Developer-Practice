package com.catherine.materialdesignapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.activities.SearchableSongsActivity;
import com.catherine.materialdesignapp.activities.UIComponentsActivity;
import com.catherine.materialdesignapp.adapters.PlaylistAdapter;
import com.catherine.materialdesignapp.components.RecyclerViewItemTouchHelper;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.view_models.PlaylistViewModel;
import com.catherine.materialdesignapp.jetpack.view_models.PlaylistViewModelFactory;
import com.catherine.materialdesignapp.listeners.OnPlaylistItemClickListener;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.catherine.materialdesignapp.utils.TextHelper;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends ChildOfMusicFragment implements OnSearchViewListener {
    public final static String TAG = PlaylistFragment.class.getSimpleName();
    private PlaylistAdapter adapter;
    private List<Playlist> playlists;
    private List<Playlist> filteredPlaylists;
    private ConstraintLayout empty_page;
    private RecyclerView recyclerView;
    private UIComponentsListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });

        empty_page = view.findViewById(R.id.empty_page);
        recyclerView = view.findViewById(R.id.rv_playlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        playlists = new ArrayList<>();
        filteredPlaylists = new ArrayList<>();
        adapter = new PlaylistAdapter(getActivity(), filteredPlaylists, new OnPlaylistItemClickListener<Playlist>() {
            @Override
            public void onItemClicked(View view, int position) {
                Log.d(TAG, "onItemClicked:" + position);
            }

            @Override
            public void onAddButtonClicked(View view, int position) {
                Log.d(TAG, "onAddButtonClicked:" + position);

                // add new songs
                Playlist playlist = filteredPlaylists.get(position);
                Intent searchableActivity = new Intent(getActivity(), SearchableSongsActivity.class);
                searchableActivity.putExtra("playlist", playlist);
                startActivity(searchableActivity);
            }

            @Override
            public void onDragged(int oldPosition, int newPosition) {
                Log.d(TAG, "onDragged:" + newPosition);
            }

            @Override
            public void onSwiped(Playlist swipedPlaylist) {
                Log.d(TAG, "onSwiped:" + swipedPlaylist);
            }
        });
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        if (UIComponentsActivity.TAG.equals(getActivity().getClass().getSimpleName()))
            listener = (UIComponentsListener) getActivity();

        // RoomDatabase
        PlaylistViewModelFactory playlistViewModelFactory = PlaylistViewModelFactory.createFactory(getActivity());
        PlaylistViewModel playlistViewModel = ViewModelProviders.of(this, playlistViewModelFactory).get(PlaylistViewModel.class);
        playlistViewModel.getAllPlaylists().observe(this, playlists -> {
            filteredPlaylists.clear();
            filteredPlaylists.addAll(playlists);
            adapter.setEntities(filteredPlaylists);
            updateList();
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filteredPlaylists.clear();
        for (Playlist playlist : playlists) {
            if (TextHelper.matcher(playlist.getName(), newText)) {
                filteredPlaylists.add(playlist);
            }
        }
        updateList();
        return false;
    }


    private void updateList() {
        // Empty playlist
        if (playlists.isEmpty() && filteredPlaylists.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            empty_page.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty_page.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onFragmentShow() {
        if (listener != null)
            listener.addOnSearchListener(this);
    }

    @Override
    public void onFragmentHide() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
