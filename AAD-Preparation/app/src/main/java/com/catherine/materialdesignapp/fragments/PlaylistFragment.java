package com.catherine.materialdesignapp.fragments;

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

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.PlaylistAdapter;
import com.catherine.materialdesignapp.components.RecyclerViewItemTouchHelper;
import com.catherine.materialdesignapp.listeners.OnPlaylistItemClickListener;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.catherine.materialdesignapp.models.Playlist;
import com.catherine.materialdesignapp.utils.TextHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        listener = (UIComponentsListener) getActivity();
        fillInData();
    }

    private void fillInData() {
        String mockData = "[\n" +
                "  {\n" +
                "    \"index\": 0,\n" +
                "    \"name\": \"Pop\",\n" +
                "    \"songs\": [\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"Love Story\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"White Horse\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"White Horse\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"You Belong with Me\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 2,\n" +
                "    \"name\": \"K-Pop\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 1,\n" +
                "    \"name\": \"Country Music\",\n" +
                "    \"songs\": [\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"Love Story\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"White Horse\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"White Horse\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"artist\": \"Taylor Swift\",\n" +
                "        \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
                "        \"album\": \"Fearless\",\n" +
                "        \"title\": \"You Belong with Me\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 5,\n" +
                "    \"name\": \"Rap\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 6,\n" +
                "    \"name\": \"R&B\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 4,\n" +
                "    \"name\": \"Trap\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Playlist>>() {
        }.getType();
        playlists = gson.fromJson(mockData, listType);
        Collections.sort(playlists);
        filteredPlaylists.clear();
        filteredPlaylists.addAll(playlists);
        adapter.setEntities(filteredPlaylists);
        updateList();
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
        adapter.notifyDataSetChanged();

        // Empty playlist
        if (playlists.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            empty_page.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onFragmentShow() {
        listener.addOnSearchListener(this);
    }

    @Override
    public void onFragmentHide() {

    }
}
