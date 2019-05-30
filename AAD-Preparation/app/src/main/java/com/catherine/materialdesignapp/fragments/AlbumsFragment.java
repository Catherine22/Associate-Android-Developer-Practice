package com.catherine.materialdesignapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.activities.AlbumDetailsActivity;
import com.catherine.materialdesignapp.adapters.AlbumAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.catherine.materialdesignapp.models.Album;
import com.catherine.materialdesignapp.utils.PrefetchSubscriber;
import com.catherine.materialdesignapp.utils.TextHelper;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class AlbumsFragment extends ChildOfMusicFragment implements OnSearchViewListener {
    private final static String TAG = AlbumsFragment.class.getSimpleName();
    private AlbumAdapter adapter;
    private OkHttpClient okHttpClient;
    private List<Album> albums;
    private List<Album> filteredAlbums;
    private PrefetchSubscriber subscriber;
    private UIComponentsListener listener;

    // firebase
    private DatabaseReference myRef;
    private ValueEventListener firebaseValueEventListener;
    private String DB_PATH = "albums";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // firebase
        myRef = database.getReference(DB_PATH);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fillInData();
            swipeRefreshLayout.setRefreshing(false);
        });
        RecyclerView recyclerView = view.findViewById(R.id.rv_artist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        albums = new ArrayList<>();
        filteredAlbums = new ArrayList<>();
        adapter = new AlbumAdapter(getActivity(), filteredAlbums, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    //Shared Elements Transitions
                    SimpleDraweeView sdv_photo = view.findViewById(R.id.sdv_photo);
                    TextView tv_name = view.findViewById(R.id.tv_title);
                    TextView tv_artist = view.findViewById(R.id.tv_subtitle);
                    String imageTransitionName = sdv_photo.getTransitionName();
                    String nameTransitionName = tv_name.getTransitionName();
                    String artistTransitionName = tv_artist.getTransitionName();


                    Intent intent = new Intent(getActivity(), AlbumDetailsActivity.class);
                    intent.putExtra("cover", adapter.getImageUrl(position));
                    intent.putExtra("album", tv_name.getText().toString());
                    intent.putExtra("artist", tv_artist.getText().toString());

                    List<String> songList = filteredAlbums.get(position).getSongs();
                    if (songList != null && songList.size() != 0) {
                        String[] songs = new String[songList.size()];
                        intent.putExtra("songs", songList.toArray(songs));
                    }

                    Pair<View, String> p1 = Pair.create(sdv_photo, imageTransitionName);
                    Pair<View, String> p2 = Pair.create(tv_name, nameTransitionName);
                    Pair<View, String> p3 = Pair.create(tv_artist, artistTransitionName);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);
                    ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        okHttpClient = new OkHttpClient.Builder().build();
        subscriber = new PrefetchSubscriber();
        listener = (UIComponentsListener) getActivity();
        fillInData();
    }

    private void fillInData() {
//        // Retrieve data from https://rallycoding.herokuapp.com/api/music_albums
//        Request request = new Request.Builder()
//                .url(Constants.ALBUM_URL)
//                .build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                updateList();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String returnMsg = String.format(Locale.ENGLISH, "connectSuccess code:%s\nisSuccessful:%b\nisRedirect:%b\ncache control:%s",
//                        response.code(), response.isSuccessful(), response.isRedirect(), response.cacheControl().toString());
//                Log.i(TAG, returnMsg);
//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<Album>>() {
//                }.getType();
//                albums = gson.fromJson(response.body().string(), listType);
//                filteredAlbums.clear();
//                filteredAlbums.addAll(albums);
//                adapter.setEntities(filteredAlbums);
//                updateList();
//                cacheItems();
//            }
//        });

        // Retrieve data from firebase realtime database
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

                albums.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Album album = child.getValue(Album.class);
                    Log.i(TAG, String.format("%s: %s", child.getKey(), album));
                    albums.add(album);
                }
                filteredAlbums.clear();
                filteredAlbums.addAll(albums);
                adapter.setEntities(filteredAlbums);
                updateList();
                cacheItems();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
    }

    private void cacheItems() {
        try {
            for (int i = 0; i < albums.size(); i++) {
//                File file = new File(Constants.ROOT_PATH + Constants.FRESCO_DIR + "/");
                String url = albums.get(i).getUrl();
                ImageRequest imageRequest = ImageRequest.fromUri(url);
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                if (resource == null || resource.size() == 0) {
                    DataSource<Void> ds = Fresco.getImagePipeline().prefetchToDiskCache(ImageRequest.fromUri(url), null);
                    ds.subscribe(subscriber, new DefaultExecutorSupplier(3).forBackgroundTasks());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filteredAlbums.clear();
        for (Album album : albums) {
            if (TextHelper.matcher(album.getArtist(), newText) || TextHelper.matcher(album.getTitle(), newText)) {
                filteredAlbums.add(album);
            }
        }
        updateList();
        return false;
    }

    private void updateList() {
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

    @Override
    public void onDestroy() {
        if (firebaseValueEventListener != null)
        myRef.removeEventListener(firebaseValueEventListener);
        super.onDestroy();
    }
}
