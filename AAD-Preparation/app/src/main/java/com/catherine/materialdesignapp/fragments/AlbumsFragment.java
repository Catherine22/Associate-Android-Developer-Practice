package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.materialdesignapp.Constants;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.AlbumAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.Album;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlbumsFragment extends Fragment {
    private final static String TAG = AlbumsFragment.class.getSimpleName();
    private AlbumAdapter adapter;
    private OkHttpClient okHttpClient;
    private List<Album> albums;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false);
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
        RecyclerView recyclerView = view.findViewById(R.id.rv_albums);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        albums = new ArrayList<>();
        adapter = new AlbumAdapter(getActivity(), albums, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        okHttpClient = new OkHttpClient.Builder().build();
        fillInData();
    }

    private void fillInData() {
        Request request = new Request.Builder()
                .url(Constants.ALBUM_URL)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String returnMsg = String.format(Locale.ENGLISH, "connectSuccess code:%s\nisSuccessful:%b\nisRedirect:%b\ncache control:%s",
                        response.code(), response.isSuccessful(), response.isRedirect(), response.cacheControl().toString());
                Log.i(TAG, returnMsg);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Album>>(){}.getType();
                albums = gson.fromJson(response.body().string(), listType);
                adapter.setEntities(albums);
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });


    }
}
