package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.PlaylistAdapter;
import com.catherine.materialdesignapp.components.RecyclerViewItemTouchHelper;
import com.catherine.materialdesignapp.listeners.OnPlaylistItemClickListener;
import com.catherine.materialdesignapp.models.Playlist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistFragment extends Fragment {
    private final static String TAG = PlaylistFragment.class.getSimpleName();
    private PlaylistAdapter adapter;
    private List<Playlist> playlists;
    private ConstraintLayout empty_page;
    private RecyclerView recyclerView;

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
        adapter = new PlaylistAdapter(getActivity(), playlists, new OnPlaylistItemClickListener() {
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
                playlists = adapter.getEntities();
            }

            @Override
            public void onSwiped(int position) {
                Log.d(TAG, "onSwiped:" + position);
                playlists = adapter.getEntities();
            }
        });
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setVisibility(View.VISIBLE);
        empty_page.setVisibility(View.GONE);
        fillInData();
    }

    private void fillInData() {
        String mockData = "[\n" +
                "  {\n" +
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
                "    \"name\": \"K-Pop\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
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
                "    \"name\": \"Rap\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"R&B\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Trap\",\n" +
                "    \"songs\": [\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Playlist>>() {
        }.getType();
        playlists = gson.fromJson(mockData, listType);
        adapter.setEntities(playlists);
        adapter.notifyDataSetChanged();


        // Empty playlist
//        recyclerView.setVisibility(View.GONE);
//        empty_page.setVisibility(View.VISIBLE);
    }
}
