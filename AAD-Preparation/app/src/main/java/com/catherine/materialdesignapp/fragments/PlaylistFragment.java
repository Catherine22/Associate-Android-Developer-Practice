package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.ArtistAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.Artist;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PlaylistFragment extends Fragment {
    private final static String TAG = ArtistsFragment.class.getSimpleName();
    private ArtistAdapter adapter;
    private List<Artist> artists;
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
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fillInData();
            swipeRefreshLayout.setRefreshing(false);
        });
        empty_page = view.findViewById(R.id.empty_page);
        recyclerView = view.findViewById(R.id.rv_playlists);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), ArtistAdapter.MAX_COLUMNS));

        artists = new ArrayList<>();
        adapter = new ArtistAdapter(getActivity(), artists, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        fillInData();
    }

    private void fillInData() {
//        String mockData = "[\n" +
//                "  {\n" +
//                "    \"artist\": \"Taylor Swift\",\n" +
//                "    \"url\": \"https://en.wikipedia.org/wiki/Taylor_Swift\",\n" +
//                "    \"image\": \"https://images-na.ssl-images-amazon.com/images/I/71YCfdyMXsL.jpg\"\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"artist\": \"Kanye West\",\n" +
//                "    \"url\": \"https://en.wikipedia.org/wiki/Kanye_West\",\n" +
//                "    \"image\": \"https://images-na.ssl-images-amazon.com/images/I/71w7GAs8SeL.jpg\"\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"artist\": \"Beyoncé\",\n" +
//                "    \"url\": \"https://en.wikipedia.org/wiki/Beyonc%C3%A9\",\n" +
//                "    \"image\": \"https://images-na.ssl-images-amazon.com/images/I/51T6RpubiVL.jpg\"\n" +
//                "  }\n" +
//                "]";
//
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<Artist>>() {
//        }.getType();
//        artists = gson.fromJson(mockData, listType);
//        adapter.setEntities(artists);
//        adapter.notifyDataSetChanged()


        recyclerView.setVisibility(View.GONE);
        empty_page.setVisibility(View.VISIBLE);
    }
}
