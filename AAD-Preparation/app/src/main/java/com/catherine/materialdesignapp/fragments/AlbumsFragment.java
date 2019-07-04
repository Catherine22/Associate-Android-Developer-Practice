package com.catherine.materialdesignapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.activities.AlbumDetailsActivity;
import com.catherine.materialdesignapp.adapters.AlbumAdapter;
import com.catherine.materialdesignapp.jetpack.entities.Album;
import com.catherine.materialdesignapp.jetpack.view_models.AlbumViewModel;
import com.catherine.materialdesignapp.jetpack.view_models.AlbumViewModelFactory;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AlbumsFragment extends ChildOfMusicFragment implements OnSearchViewListener {
    public final static String TAG = AlbumsFragment.class.getSimpleName();
    private UIComponentsListener listener;
    private AlbumViewModel albumViewModel;

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
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
        RecyclerView recyclerView = view.findViewById(R.id.rv_artist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AlbumAdapter adapter = new AlbumAdapter(this::goToAlbumDetails);
        recyclerView.setAdapter(adapter);

        if (getActivity() == null)
            return;

        listener = (UIComponentsListener) getActivity();
        // special case, this fragment will be called at first lunch, which means onFragmentShow() won't be triggered
        listener.addOnSearchListener(this);

        // RoomDatabase
        AlbumViewModelFactory albumViewModelFactory = AlbumViewModelFactory.createFactory(getActivity());
        albumViewModel = ViewModelProviders.of(this, albumViewModelFactory).get(AlbumViewModel.class);
        albumViewModel.getAlbumLiveData().observe(this, adapter::submitList);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        albumViewModel.search(newText);
        return false;
    }

    @Override
    public void onFragmentShow() {
        if (listener != null)
            listener.addOnSearchListener(this);
    }

    @Override
    public void onFragmentHide() {
        if (listener != null)
            listener.addOnSearchListener(null);
    }

    private void goToAlbumDetails(View view, Album album) {
        SimpleDraweeView sdv_photo = view.findViewById(R.id.sdv_photo);
        TextView tv_name = view.findViewById(R.id.tv_title);
        TextView tv_artist = view.findViewById(R.id.tv_subtitle);

        Intent intent = new Intent(getActivity(), AlbumDetailsActivity.class);
        intent.putExtra("cover", album.getImage());
        intent.putExtra("album", album.getTitle());
        intent.putExtra("artist", album.getArtist());

        List<String> songList = album.getSongs();
        String[] songs = new String[songList.size()];
        intent.putExtra("songs", songList.toArray(songs));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Shared Elements Transitions
            String imageTransitionName = sdv_photo.getTransitionName();
            String nameTransitionName = tv_name.getTransitionName();
            String artistTransitionName = tv_artist.getTransitionName();

            Pair<View, String> p1 = Pair.create(sdv_photo, imageTransitionName);
            Pair<View, String> p2 = Pair.create(tv_name, nameTransitionName);
            Pair<View, String> p3 = Pair.create(tv_artist, artistTransitionName);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
