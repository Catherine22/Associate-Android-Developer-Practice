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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.activities.SearchableActivity;
import com.catherine.materialdesignapp.activities.UIComponentsActivity;
import com.catherine.materialdesignapp.adapters.PlaylistAdapter;
import com.catherine.materialdesignapp.components.RecyclerViewItemTouchHelper;
import com.catherine.materialdesignapp.listeners.OnPlaylistItemClickListener;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.catherine.materialdesignapp.models.Playlist;
import com.catherine.materialdesignapp.utils.TextHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistFragment extends ChildOfMusicFragment implements OnSearchViewListener {
    private final static String TAG = PlaylistFragment.class.getSimpleName();
    private PlaylistAdapter adapter;
    private List<Playlist> playlists;
    private List<Playlist> filteredPlaylists;
    private ConstraintLayout empty_page;
    private RecyclerView recyclerView;
    private UIComponentsListener listener;

    // firebase
    private DatabaseReference myRef;
    private ValueEventListener firebaseValueEventListener;
    private String DB_PATH = "test_playlist"; // TODO playlists

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(DB_PATH);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fillInData();
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
                Intent searchableActivity = new Intent(getActivity(), SearchableActivity.class);
                searchableActivity.putExtra("playlist", playlist);
                startActivity(searchableActivity);

                // grab available songs from db/songs/
                // add songs to playlist.getSongs()
                // update to firebase ->  myRef.child(playlist.getName()).setValue(playlist);

            }

            @Override
            public void onDragged(int oldPosition, int newPosition) {
                Log.d(TAG, "onDragged:" + newPosition);

                // update raw data
                int header = 0;
                while (header < playlists.size()) {
                    if (playlists.get(header).getIndex() == oldPosition) {
                        playlists.get(header).setIndex(newPosition);
                    }
                    if (playlists.get(header).getIndex() == newPosition) {
                        playlists.get(header).setIndex(oldPosition);
                    }
                    header++;
                }

                // update filtered data
                int temp = filteredPlaylists.get(oldPosition).getIndex();
                filteredPlaylists.get(oldPosition).setIndex(newPosition);
                filteredPlaylists.get(newPosition).setIndex(temp);


                // Put the latest playlists to the server
            }

            @Override
            public void onSwiped(Playlist swipedPlaylist) {
                Log.d(TAG, "onSwiped:" + swipedPlaylist);

                // update raw data
                int header = 0;
                while (header < playlists.size()) {
                    if (playlists.get(header).equals(swipedPlaylist)) {
                        playlists.remove(header);
                        break;
                    }
                    header++;
                }

                // update filtered data
                filteredPlaylists = adapter.getEntities();


                // Put the latest playlists to the server
            }
        });
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setVisibility(View.VISIBLE);
        empty_page.setVisibility(View.GONE);

        if (UIComponentsActivity.TAG.equals(getActivity().getClass().getSimpleName()))
            listener = (UIComponentsListener) getActivity();
        fillInData();
    }

    private void fillInData() {
        if (firebaseValueEventListener != null)
            myRef.removeEventListener(firebaseValueEventListener);

        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        // Failed to read value
        firebaseValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                playlists.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Playlist playlist = child.getValue(Playlist.class);
                    Log.i(TAG, String.format("%s: %s", child.getKey(), playlist));

                    playlists.add(playlist);
                }
                Collections.sort(playlists);
                filteredPlaylists.clear();
                filteredPlaylists.addAll(playlists);
                adapter.setEntities(filteredPlaylists);
                updateList();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                updateList();
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
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
        if (playlists.isEmpty()) {
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
}
