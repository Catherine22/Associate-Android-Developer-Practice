package com.catherine.materialdesignapp.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.view_models.PlaylistViewModel;
import com.catherine.materialdesignapp.jetpack.view_models.PlaylistViewModelFactory;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.google.android.material.snackbar.Snackbar;

public class PlaylistFragment extends ChildOfMusicFragment implements OnSearchViewListener {
    public final static String TAG = PlaylistFragment.class.getSimpleName();
    private ConstraintLayout empty_page;
    private UIComponentsListener listener;
    private RecyclerView recyclerView;
    private PlaylistViewModel playlistViewModel;

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
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));

        empty_page = view.findViewById(R.id.empty_page);
        recyclerView = view.findViewById(R.id.rv_playlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        PlaylistAdapter adapter = new PlaylistAdapter(this::goToPlaylist);
        recyclerView.setAdapter(adapter);
        initAction();

        if (getActivity() == null)
            return;

        if (UIComponentsActivity.TAG.equals(getActivity().getClass().getSimpleName()))
            listener = (UIComponentsListener) getActivity();

        // RoomDatabase
        PlaylistViewModelFactory playlistViewModelFactory = PlaylistViewModelFactory.createFactory(getActivity());
        playlistViewModel = ViewModelProviders.of(this, playlistViewModelFactory).get(PlaylistViewModel.class);
        playlistViewModel.getPlaylistLiveData().observe(this, playlists -> {
            if (playlists.getLoadedCount() == 0) {
                recyclerView.setVisibility(View.INVISIBLE);
                empty_page.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                empty_page.setVisibility(View.INVISIBLE);
                adapter.submitList(playlists);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        playlistViewModel.search(newText);
        return false;
    }

    private void goToPlaylist(View view, Playlist playlist) {
        Intent searchableActivity = new Intent(getActivity(), SearchableSongsActivity.class);
        searchableActivity.putExtra("playlist", playlist);
        startActivity(searchableActivity);
    }

    @Override
    public void onFragmentShow() {
        if (listener != null)
            listener.setOnSearchListener(this);
    }

    @Override
    public void onFragmentHide() {
    }

    // TODO: not working
    private void initAction() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Playlist playlist = ((PlaylistAdapter.MainRvHolder) viewHolder).getPlaylist();
                playlistViewModel.delete(playlist);

                String text = getString(R.string.undo_deleted_playlist, playlist.getName());
                Snackbar.make(recyclerView, getString(R.string.undo), Snackbar.LENGTH_LONG)
                        .setAction(text, view -> playlistViewModel.insert(playlist)).show();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
