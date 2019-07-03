package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.AlbumAdapter;
import com.catherine.materialdesignapp.jetpack.view_models.AlbumViewModel;
import com.catherine.materialdesignapp.jetpack.view_models.AlbumViewModelFactory;
import com.catherine.materialdesignapp.listeners.OnSearchViewListener;
import com.catherine.materialdesignapp.listeners.UIComponentsListener;

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

        AlbumAdapter adapter = new AlbumAdapter();
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
}
